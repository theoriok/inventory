package org.theoriok.inventory.persistence.adapters;

import org.springframework.stereotype.Component;
import org.theoriok.inventory.BookId;
import org.theoriok.inventory.domain.Book;
import org.theoriok.inventory.domain.BookBuilder;
import org.theoriok.inventory.persistence.entities.BookEntity;
import org.theoriok.inventory.persistence.repositories.BookRepository;
import org.theoriok.inventory.port.PersistBookPort;

import java.util.Collection;
import java.util.Optional;

@Component
public class PersistBookAdapter implements PersistBookPort {
    private final BookRepository bookRepository;

    public PersistBookAdapter(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public Collection<Book> findAll() {
        return bookRepository.findAll().stream()
            .map(this::toDomainObject)
            .toList();
    }

    @Override
    public Optional<Book> findById(BookId businessId) {
        return bookRepository.findByBusinessId(businessId.value()).map(this::toDomainObject);
    }

    @Override
    public void upsert(Book book) {
        var entity = toEntity(book);
        bookRepository.findByBusinessId(book.businessId().value()).ifPresent(foundEntity -> entity.setId(foundEntity.getId()));
        bookRepository.save(entity);
    }

    @Override
    public boolean delete(BookId bookId) {
        Optional<BookEntity> bookEntity = bookRepository.findByBusinessId(bookId.value());
        bookEntity.ifPresent(bookRepository::delete);
        return bookEntity.isPresent();
    }

    private BookEntity toEntity(Book domainObject) {
        return new BookEntity(
            domainObject.businessId().value(),
            domainObject.title(),
            domainObject.author(),
            domainObject.description()
        );
    }

    private Book toDomainObject(BookEntity entity) {
        return BookBuilder.builder()
            .businessId(new BookId(entity.getBusinessId()))
            .title(entity.getTitle())
            .author(entity.getAuthor())
            .description(entity.getDescription())
            .build();
    }
}
