package org.theoriok.inventory.mappers;

import org.apache.commons.lang3.NotImplementedException;
import org.springframework.stereotype.Component;
import org.theoriok.inventory.domain.Book;
import org.theoriok.inventory.query.FindBooks;

import java.util.Collection;

@Component
public class BookCommandMapper implements CommandMapper<Book, Object, FindBooks.ListResponse, Object> {
    @Override
    public Object toSingleResponse(Book domainObject) {
        throw new NotImplementedException();
    }

    @Override
    public FindBooks.ListResponse toListResponse(Collection<Book> domainObjects) {
        return new FindBooks.ListResponse(domainObjects.stream().map(this::toResponseBook).toList());
    }

    private FindBooks.Book toResponseBook(Book book) {
        return new FindBooks.Book(book.businessId(), book.title(), book.author(), book.description());
    }

    @Override
    public Book toDomainObject(Object dto) {
        throw new NotImplementedException();
    }
}
