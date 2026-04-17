package org.theoriok.inventory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.theoriok.inventory.client.ApiException;
import org.theoriok.inventory.client.model.CreateBook;
import org.theoriok.inventory.client.model.UpdateBook;
import org.theoriok.inventory.persistence.entities.BookEntity;

class BookContractTest extends ContractTest {

    @Nested
    class Find {
        @Test
        void shouldReturnEmptyListWhenNoBooksExist() throws ApiException {
            var books = bookApi.findBooks();

            assertThat(books).isEmpty();
        }

        @Test
        void shouldReturnBookWhenBookExists() throws ApiException {
            var inserted = jdbcAggregateTemplate.insert(testBook());

            var books = bookApi.findBooks();

            assertThat(books).singleElement().satisfies(book -> {
                assertThat(book.getId()).isEqualTo(inserted.id().toString());
                assertThat(book.getTitle()).isEqualTo("The Hobbit");
                assertThat(book.getAuthor()).isEqualTo("JRR Tolkien");
                assertThat(book.getDescription()).isEqualTo("In a hole under the ground, there lived a Hobbit.");
            });
        }

        @Test
        void shouldReturnBookById() throws ApiException {
            var book = jdbcAggregateTemplate.insert(testBook());

            var result = bookApi.findBookById(book.id().toString());

            assertThat(result.getId()).isEqualTo(book.id().toString());
            assertThat(result.getTitle()).isEqualTo("The Hobbit");
            assertThat(result.getAuthor()).isEqualTo("JRR Tolkien");
            assertThat(result.getDescription()).isEqualTo("In a hole under the ground, there lived a Hobbit.");
        }

        @Test
        void shouldReturn404WhenBookNotFound() {
            var randomId = BookId.randomBookId();

            assertThatThrownBy(() -> bookApi.findBookById(randomId.value()))
                .isInstanceOfSatisfying(ApiException.class, exception -> {
                    assertThat(exception.getCode()).isEqualTo(404);
                    var problem = parseProblem(exception);
                    assertThat(problem.getTitle()).isEqualTo("Not Found");
                    assertThat(problem.getStatus()).isEqualTo(404);
                    assertThat(problem.getDetail()).isEqualTo("Book %s not found".formatted(randomId.value()));
                    assertThat(problem.getInstance()).isEqualTo("/books/%s".formatted(randomId.value()));
                });
        }
    }

    @Nested
    class Create {
        @Test
        void shouldCreateBook() throws ApiException {
            var createBook = new CreateBook()
                .title("The Hobbit")
                .author("JRR Tolkien")
                .description("In a hole under the ground, there lived a Hobbit.");

            var result = bookApi.createBook(createBook);

            assertThat(result.getId()).isNotNull();
            assertThat(result.getTitle()).isEqualTo("The Hobbit");
            assertThat(result.getAuthor()).isEqualTo("JRR Tolkien");
            assertThat(result.getDescription()).isEqualTo("In a hole under the ground, there lived a Hobbit.");
        }

        @Test
        void shouldReturn400WhenValidationFails() {
            var createBook = new CreateBook()
                .title("")
                .author("")
                .description("");

            assertThatThrownBy(() -> bookApi.createBook(createBook))
                .isInstanceOfSatisfying(ApiException.class, exception -> {
                    assertThat(exception.getCode()).isEqualTo(400);
                    var problem = parseProblem(exception);
                    assertThat(problem.getTitle()).isEqualTo("Bad Request");
                    assertThat(problem.getStatus()).isEqualTo(400);
                    assertThat(problem.getDetail()).isEqualTo("Validation failed");
                    assertThat(problem.getInstance()).isEqualTo("/books");
                    assertThat(problem.getErrors()).containsEntry("title", "must not be blank")
                        .containsEntry("author", "must not be blank")
                        .containsEntry("description", "must not be blank");
                });
        }
    }

    @Nested
    class Update {
        @Test
        void shouldUpdateExistingBook() throws ApiException {
            var book = jdbcAggregateTemplate.insert(testBook());
            var updateBook = new UpdateBook()
                .title("Updated Title")
                .author("Updated Author")
                .description("Updated Description");

            bookApi.updateBook(book.id().toString(), updateBook);

            var updated = bookApi.findBookById(book.id().toString());
            assertThat(updated.getTitle()).isEqualTo("Updated Title");
            assertThat(updated.getAuthor()).isEqualTo("Updated Author");
            assertThat(updated.getDescription()).isEqualTo("Updated Description");
        }

        @Test
        void shouldReturn404WhenUpdatingNonExistentBook() {
            var randomId = BookId.randomBookId();
            var updateBook = new UpdateBook()
                .title("Title")
                .author("Author")
                .description("Description");

            assertThatThrownBy(() -> bookApi.updateBook(randomId.value(), updateBook))
                .isInstanceOfSatisfying(ApiException.class, exception -> {
                    assertThat(exception.getCode()).isEqualTo(404);
                    var problem = parseProblem(exception);
                    assertThat(problem.getTitle()).isEqualTo("Not Found");
                    assertThat(problem.getStatus()).isEqualTo(404);
                    assertThat(problem.getDetail()).isEqualTo("Book %s not found".formatted(randomId.value()));
                    assertThat(problem.getInstance()).isEqualTo("/books/%s".formatted(randomId.value()));
                });
        }

        @Test
        void shouldReturn400WhenValidationFails() {
            var book = jdbcAggregateTemplate.insert(testBook());
            var updateBook = new UpdateBook()
                .title("")
                .author("")
                .description("");

            assertThatThrownBy(() -> bookApi.updateBook(book.id().toString(), updateBook))
                .isInstanceOfSatisfying(ApiException.class, exception -> {
                    assertThat(exception.getCode()).isEqualTo(400);
                    var problem = parseProblem(exception);
                    assertThat(problem.getTitle()).isEqualTo("Bad Request");
                    assertThat(problem.getStatus()).isEqualTo(400);
                    assertThat(problem.getDetail()).isEqualTo("Validation failed");
                    assertThat(problem.getInstance()).isEqualTo("/books/%s".formatted(book.id()));
                    assertThat(problem.getErrors()).containsEntry("title", "must not be blank")
                        .containsEntry("author", "must not be blank")
                        .containsEntry("description", "must not be blank");
                });
        }
    }

    @Nested
    class Delete {
        @Test
        void shouldDeleteExistingBook() throws ApiException {
            var book = jdbcAggregateTemplate.insert(testBook());

            bookApi.deleteBookById(book.id().toString());

            assertThat(bookApi.findBooks()).isEmpty();
        }

        @Test
        void shouldReturn404WhenDeletingNonExistentBook() {
            var randomId = BookId.randomBookId();

            assertThatThrownBy(() -> bookApi.deleteBookById(randomId.value()))
                .isInstanceOfSatisfying(ApiException.class, exception -> {
                    assertThat(exception.getCode()).isEqualTo(404);
                    var problem = parseProblem(exception);
                    assertThat(problem.getTitle()).isEqualTo("Not Found");
                    assertThat(problem.getStatus()).isEqualTo(404);
                    assertThat(problem.getDetail()).isEqualTo("Book %s not found".formatted(randomId.value()));
                    assertThat(problem.getInstance()).isEqualTo("/books/%s".formatted(randomId.value()));
                });
        }
    }

    private BookEntity testBook() {
        return new BookEntity(BookId.randomBookId().toUuid(), "The Hobbit", "JRR Tolkien", "In a hole under the ground, there lived a Hobbit.");
    }
}
