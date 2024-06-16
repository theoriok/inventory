package org.theoriok.inventory.mappers;

import org.springframework.stereotype.Component;
import org.theoriok.inventory.command.UpsertBook;
import org.theoriok.inventory.domain.Book;
import org.theoriok.inventory.domain.BookBuilder;
import org.theoriok.inventory.query.FindBooks;

import java.util.Collection;

@Component
public class BookCommandMapper implements CommandMapper<Book, UpsertBook.Request, FindBooks.ListResponse, Object> {
    @Override
    public FindBooks.SingleResponse toSingleResponse(Book domainObject) {
        return new FindBooks.SingleResponse(toResponseBook(domainObject));
    }

    @Override
    public FindBooks.ListResponse toListResponse(Collection<Book> domainObjects) {
        return new FindBooks.ListResponse(domainObjects.stream().map(this::toResponseBook).toList());
    }

    private FindBooks.Book toResponseBook(Book book) {
        return new FindBooks.Book(book.businessId(), book.title(), book.author(), book.description());
    }

    @Override
    public Book toDomainObject(UpsertBook.Request dto) {
        return BookBuilder.builder()
            .businessId(dto.businessId())
            .title(dto.title())
            .author(dto.author())
            .description(dto.description())
            .build();
    }
}
