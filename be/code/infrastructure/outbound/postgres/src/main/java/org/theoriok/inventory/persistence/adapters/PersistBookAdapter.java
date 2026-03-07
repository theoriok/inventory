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
    public Optional<Book> findById(BookId id) {
        return bookRepository.findById(id.toUuid()).map(this::toDomainObject);
    }

    @Override
    public Book create(Book book) {
        var entity = toEntity(book);
        bookRepository.save(entity);
        return book;
    }

    @Override
    public boolean update(Book book) {
        var entity = toEntity(book);
        bookRepository.save(entity);
        return true;
    }

    @Override
    public boolean delete(BookId id) {
        Optional<BookEntity> bookEntity = bookRepository.findById(id.toUuid());
        bookEntity.ifPresent(bookRepository::delete);
        return bookEntity.isPresent();
    }

    private BookEntity toEntity(Book book) {
        return new BookEntity(
            book.id().toUuid(),
            book.title(),
            book.author(),
            book.description()
        );
    }

    private Book toDomainObject(BookEntity entity) {
        return BookBuilder.builder()
            .id(BookId.from(entity.getId()))
            .title(entity.getTitle())
            .author(entity.getAuthor())
            .description(entity.getDescription())
            .build();
    }
}
