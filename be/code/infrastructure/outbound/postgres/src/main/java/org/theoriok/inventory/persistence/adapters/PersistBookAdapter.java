package org.theoriok.inventory.persistence.adapters;

import org.springframework.data.jdbc.core.JdbcAggregateOperations;
import org.springframework.stereotype.Component;
import org.theoriok.inventory.BookId;
import org.theoriok.inventory.domain.Book;
import org.theoriok.inventory.domain.BookBuilder;
import org.theoriok.inventory.persistence.entities.BookEntity;
import org.theoriok.inventory.persistence.repositories.BookRepository;
import org.theoriok.inventory.port.PersistBookPort;

import java.util.List;
import java.util.Optional;

@Component
public class PersistBookAdapter implements PersistBookPort {
    private final BookRepository bookRepository;
    private final JdbcAggregateOperations jdbcAggregateTemplate;

    public PersistBookAdapter(BookRepository bookRepository, JdbcAggregateOperations jdbcAggregateTemplate) {
        this.bookRepository = bookRepository;
        this.jdbcAggregateTemplate = jdbcAggregateTemplate;
    }

    @Override
    public List<Book> findAll() {
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
        jdbcAggregateTemplate.insert(toEntity(book));
        return book;
    }

    @Override
    public void update(Book book) {
        jdbcAggregateTemplate.update(toEntity(book));
    }

    @Override
    public boolean delete(BookId id) {
        return bookRepository.deleteByIdReturningCount(id.toUuid()) > 0;
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
            .id(BookId.from(entity.id()))
            .title(entity.title())
            .author(entity.author())
            .description(entity.description())
            .build();
    }
}
