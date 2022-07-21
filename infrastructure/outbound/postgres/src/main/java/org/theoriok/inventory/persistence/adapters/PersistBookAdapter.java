package org.theoriok.inventory.persistence.adapters;

import org.springframework.stereotype.Component;
import org.theoriok.inventory.domain.Book;
import org.theoriok.inventory.persistence.mappers.BookEntityMapper;
import org.theoriok.inventory.persistence.repositories.BookRepository;
import org.theoriok.inventory.port.PersistBookPort;

import java.util.Collection;
import java.util.Optional;

@Component
public class PersistBookAdapter implements PersistBookPort {
    private final BookEntityMapper bookDomainMapper;
    private final BookRepository bookRepository;

    public PersistBookAdapter(BookEntityMapper bookDomainMapper, BookRepository bookRepository) {
        this.bookDomainMapper = bookDomainMapper;
        this.bookRepository = bookRepository;
    }

    @Override
    public Collection<Book> findAll() {
        return bookDomainMapper.toDomainObjects(bookRepository.findAll());
    }

    @Override
    public Optional<Book> findById(String businessId) {
        return bookRepository.findByBusinessId(businessId).map(bookDomainMapper::toDomainObject);
    }

    @Override
    public void upsert(Book book) {
        var entity = bookDomainMapper.toEntity(book);
        bookRepository.findByBusinessId(book.businessId()).ifPresent(foundEntity -> entity.setId(foundEntity.getId()));
        bookRepository.save(entity);
    }
}
