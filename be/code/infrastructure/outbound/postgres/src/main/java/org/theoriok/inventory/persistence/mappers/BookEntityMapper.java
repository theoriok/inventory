package org.theoriok.inventory.persistence.mappers;

import org.springframework.stereotype.Component;
import org.theoriok.inventory.BookId;
import org.theoriok.inventory.domain.Book;
import org.theoriok.inventory.domain.BookBuilder;
import org.theoriok.inventory.persistence.entities.BookEntity;

@Component
public class BookEntityMapper implements EntityMapper<Book, BookEntity> {

    @Override
    public BookEntity toEntity(Book domainObject) {
        return new BookEntity(
            domainObject.businessId().value(),
            domainObject.title(),
            domainObject.author(),
            domainObject.description()
        );
    }

    @Override
    public Book toDomainObject(BookEntity entity) {
        return BookBuilder.builder()
            .businessId(new BookId(entity.getBusinessId()))
            .title(entity.getTitle())
            .author(entity.getAuthor())
            .description(entity.getDescription())
            .build();
    }
}
