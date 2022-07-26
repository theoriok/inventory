package org.theoriok.inventory.web.adapters;

import io.micrometer.core.annotation.Timed;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.theoriok.inventory.command.DeleteBook;
import org.theoriok.inventory.command.UpsertBook;
import org.theoriok.inventory.query.FindBooks;
import org.theoriok.inventory.web.dto.BookDto;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/books")
@Timed
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
    public ResponseEntity<BookDto> findBookById(@PathVariable String id) {
        return findBooks.findById(id)
            .map(FindBooks.SingleResponse::book)
            .map(this::toBookDto)
            .map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBookById(@PathVariable String id) {
        deleteBook.delete(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping
    public ResponseEntity<Void> upsertBook(@RequestBody BookDto bookDto) {
        upsertBook.upsert(toUpsertRequest(bookDto));
        return ResponseEntity.noContent().build();
    }

    private UpsertBook.Request toUpsertRequest(BookDto bookDto) {
        return new UpsertBook.Request(
            bookDto.getBusinessId(),
            bookDto.getTitle(),
            bookDto.getAuthor(),
            bookDto.getDescription()
        );
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
