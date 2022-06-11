package org.theoriok.inventory.web.adapters;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.theoriok.inventory.query.FindBooks;
import org.theoriok.inventory.web.dto.BookDto;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/books")
public class BookController {

    private final FindBooks findBooks;

    public BookController(FindBooks findBooks) {
        this.findBooks = findBooks;
    }

    @GetMapping
    public ResponseEntity<Collection<BookDto>> findBooks() {
        var booksResponse = findBooks.findAll();
        return ResponseEntity.ok(toBookDtos(booksResponse));
    }

    private List<BookDto> toBookDtos(FindBooks.ListResponse listResponse) {
        return listResponse.books().stream()
            .map(this::toBookDto)
            .toList();
    }

    private BookDto toBookDto(FindBooks.Book book) {
        return new BookDto(
            book.businessId(),
            book.title(),
            book.author(),
            book.description()
        );
    }

}
