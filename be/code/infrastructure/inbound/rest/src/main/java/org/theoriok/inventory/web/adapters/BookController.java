package org.theoriok.inventory.web.adapters;

import static org.springframework.http.HttpStatus.NOT_FOUND;

import io.micrometer.core.annotation.Timed;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.theoriok.inventory.BookId;
import org.theoriok.inventory.command.DeleteBook;
import org.theoriok.inventory.command.UpsertBook;
import org.theoriok.inventory.domain.Book;
import org.theoriok.inventory.query.FindBooks;
import org.theoriok.inventory.web.dto.BookDto;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/books")
@Timed
@CrossOrigin(origins = "http://localhost:3000")
public class BookController {

    private final FindBooks findBooks;
    private final UpsertBook upsertBook;
    private final DeleteBook deleteBook;

    public BookController(FindBooks findBooks, UpsertBook upsertBook, DeleteBook deleteBook) {
        this.findBooks = findBooks;
        this.upsertBook = upsertBook;
        this.deleteBook = deleteBook;
    }

    @GetMapping
    public ResponseEntity<Collection<BookDto>> findBooks() {
        var booksResponse = findBooks.findAll();
        return ResponseEntity.ok(toBookDtos(booksResponse));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookDto> findBookById(@PathVariable(name = "id") String id) {
        return findBooks.findById(new BookId(id))
            .map(this::toBookDto)
            .map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.of(ProblemDetail.forStatus(NOT_FOUND)).build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBookById(@PathVariable(name = "id") String id) {
        return switch (deleteBook.delete(new BookId(id))) {
            case DELETED -> ResponseEntity.ok().build();
            case NOT_FOUND -> ResponseEntity.of(ProblemDetail.forStatus(NOT_FOUND)).build();
        };
    }

    @PutMapping
    public ResponseEntity<Void> upsertBook(@RequestBody BookDto bookDto) {
        upsertBook.upsert(toUpsertRequest(bookDto));
        return ResponseEntity.noContent().build();
    }

    private UpsertBook.Request toUpsertRequest(BookDto bookDto) {
        return new UpsertBook.Request(
            new BookId(bookDto.businessId()),
            bookDto.title(),
            bookDto.author(),
            bookDto.description()
        );
    }

    private List<BookDto> toBookDtos(List<Book> listResponse) {
        return listResponse.stream()
            .map(this::toBookDto)
            .toList();
    }

    private BookDto toBookDto(Book book) {
        return new BookDto(
            book.businessId().value(),
            book.title(),
            book.author(),
            book.description()
        );
    }
}
