package org.theoriok.inventory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.theoriok.inventory.client.ApiException;
import org.theoriok.inventory.client.model.CreateCap;
import org.theoriok.inventory.client.model.UpdateCap;
import org.theoriok.inventory.persistence.entities.CapEntity;
import org.theoriok.inventory.persistence.entities.CountryEntity;

import java.util.UUID;

class CapContractTest extends ContractTest {

    @Nested
    class Find {
        @Test
        void shouldReturnEmptyListWhenNoCapsExist() throws ApiException {
            var caps = capApi.findCaps(null);

            assertThat(caps).isEmpty();
        }

        @Test
        void shouldReturnCapWhenCapExists() throws ApiException {
            var country = jdbcAggregateTemplate.insert(testCountry());
            var cap = jdbcAggregateTemplate.insert(testCap(country));

            var caps = capApi.findCaps(null);

            assertThat(caps).singleElement().satisfies(result -> {
                assertThat(result.getId()).isEqualTo(cap.id().toString());
                assertThat(result.getName()).isEqualTo("Belgian Cap");
                assertThat(result.getDescription()).isEqualTo("This is a Belgian Cap");
                assertThat(result.getAmount()).isEqualTo(5);
                assertThat(result.getCountry().getName()).isEqualTo("Belgium");
                assertThat(result.getCountry().getCode()).isEqualTo("BE");
            });
        }

        @Test
        void shouldReturnCapsByCountry() throws ApiException {
            var belgium = jdbcAggregateTemplate.insert(testCountry("BE"));
            var belgianCap = jdbcAggregateTemplate.insert(testCap(belgium));
            var netherlands = jdbcAggregateTemplate.insert(testCountry("NL"));
            jdbcAggregateTemplate.insert(testCap(netherlands));

            var caps = capApi.findCaps("BE");

            assertThat(caps).singleElement().satisfies(result ->
                assertThat(result.getId()).isEqualTo(belgianCap.id().toString()));
        }

        @Test
        void shouldReturnCapById() throws ApiException {
            var country = jdbcAggregateTemplate.insert(testCountry());
            var cap = jdbcAggregateTemplate.insert(testCap(country));

            var result = capApi.findCapById(cap.id().toString());

            assertThat(result.getId()).isEqualTo(cap.id().toString());
            assertThat(result.getName()).isEqualTo("Belgian Cap");
            assertThat(result.getDescription()).isEqualTo("This is a Belgian Cap");
            assertThat(result.getAmount()).isEqualTo(5);
            assertThat(result.getCountry().getName()).isEqualTo("Belgium");
            assertThat(result.getCountry().getCode()).isEqualTo("BE");
        }

        @Test
        void shouldReturn404WhenCapNotFound() {
            var randomId = CapId.randomCapId();

            assertThatThrownBy(() -> capApi.findCapById(randomId.value()))
                .isInstanceOfSatisfying(ApiException.class, exception -> {
                    assertThat(exception.getCode()).isEqualTo(404);
                    var problem = parseProblem(exception);
                    assertThat(problem.getTitle()).isEqualTo("Not Found");
                    assertThat(problem.getStatus()).isEqualTo(404);
                    assertThat(problem.getDetail()).isEqualTo("Cap %s not found".formatted(randomId.value()));
                    assertThat(problem.getInstance()).isEqualTo("/caps/%s".formatted(randomId.value()));
                });
        }
    }

    @Nested
    class Create {
        @Test
        void shouldCreateCap() throws ApiException {
            jdbcAggregateTemplate.insert(testCountry());
            var createCap = new CreateCap()
                .name("Belgian Cap")
                .description("This is a Belgian Cap")
                .amount(5)
                .country("BE");

            var result = capApi.createCap(createCap);

            assertThat(result.getId()).isNotNull();
            assertThat(result.getName()).isEqualTo("Belgian Cap");
            assertThat(result.getDescription()).isEqualTo("This is a Belgian Cap");
            assertThat(result.getAmount()).isEqualTo(5);
            assertThat(result.getCountry().getName()).isEqualTo("Belgium");
            assertThat(result.getCountry().getCode()).isEqualTo("BE");
        }

        @Test
        void shouldReturn400WhenValidationFails() {
            var createCap = new CreateCap()
                .name("")
                .description("")
                .amount(1)
                .country("");

            assertThatThrownBy(() -> capApi.createCap(createCap))
                .isInstanceOfSatisfying(ApiException.class, exception -> {
                    assertThat(exception.getCode()).isEqualTo(400);
                    var problem = parseProblem(exception);
                    assertThat(problem.getTitle()).isEqualTo("Bad Request");
                    assertThat(problem.getStatus()).isEqualTo(400);
                    assertThat(problem.getDetail()).isEqualTo("Validation failed");
                    assertThat(problem.getInstance()).isEqualTo("/caps");
                    assertThat(problem.getErrors()).containsEntry("name", "must not be blank")
                        .containsEntry("description", "must not be blank")
                        .containsEntry("country", "must not be blank");
                });
        }

        @Test
        void shouldReturn400WhenCountryUnknown() {
            var createCap = new CreateCap()
                .name("Belgian Cap")
                .description("This is a Belgian Cap")
                .amount(5)
                .country("XX");

            assertThatThrownBy(() -> capApi.createCap(createCap))
                .isInstanceOfSatisfying(ApiException.class, exception -> {
                    assertThat(exception.getCode()).isEqualTo(400);
                    var problem = parseProblem(exception);
                    assertThat(problem.getTitle()).isEqualTo("Bad Request");
                    assertThat(problem.getStatus()).isEqualTo(400);
                    assertThat(problem.getDetail()).isEqualTo("Unknown country XX");
                    assertThat(problem.getInstance()).isEqualTo("/caps");
                });
        }
    }

    @Nested
    class Update {
        @Test
        void shouldUpdateExistingCap() throws ApiException {
            var country = jdbcAggregateTemplate.insert(testCountry());
            var cap = jdbcAggregateTemplate.insert(testCap(country));
            var updateCap = new UpdateCap()
                .name("Updated Cap")
                .description("Updated description")
                .amount(10)
                .country("BE");

            capApi.updateCap(cap.id().toString(), updateCap);

            var updated = capApi.findCapById(cap.id().toString());
            assertThat(updated.getName()).isEqualTo("Updated Cap");
            assertThat(updated.getDescription()).isEqualTo("Updated description");
            assertThat(updated.getAmount()).isEqualTo(10);
        }

        @Test
        void shouldReturn404WhenUpdatingNonExistentCap() {
            var randomId = CapId.randomCapId();
            var updateCap = new UpdateCap()
                .name("Name")
                .description("Description")
                .amount(1)
                .country("BE");

            assertThatThrownBy(() -> capApi.updateCap(randomId.value(), updateCap))
                .isInstanceOfSatisfying(ApiException.class, exception -> {
                    assertThat(exception.getCode()).isEqualTo(404);
                    var problem = parseProblem(exception);
                    assertThat(problem.getTitle()).isEqualTo("Not Found");
                    assertThat(problem.getStatus()).isEqualTo(404);
                    assertThat(problem.getDetail()).isEqualTo("Cap %s not found".formatted(randomId.value()));
                    assertThat(problem.getInstance()).isEqualTo("/caps/%s".formatted(randomId.value()));
                });
        }

        @Test
        void shouldReturn400WhenValidationFails() {
            var country = jdbcAggregateTemplate.insert(testCountry());
            var cap = jdbcAggregateTemplate.insert(testCap(country));
            var updateCap = new UpdateCap()
                .name("")
                .description("")
                .amount(1)
                .country("");

            assertThatThrownBy(() -> capApi.updateCap(cap.id().toString(), updateCap))
                .isInstanceOfSatisfying(ApiException.class, exception -> {
                    assertThat(exception.getCode()).isEqualTo(400);
                    var problem = parseProblem(exception);
                    assertThat(problem.getTitle()).isEqualTo("Bad Request");
                    assertThat(problem.getStatus()).isEqualTo(400);
                    assertThat(problem.getDetail()).isEqualTo("Validation failed");
                    assertThat(problem.getInstance()).isEqualTo("/caps/%s".formatted(cap.id()));
                    assertThat(problem.getErrors()).containsEntry("name", "must not be blank")
                        .containsEntry("description", "must not be blank")
                        .containsEntry("country", "must not be blank");
                });
        }

        @Test
        void shouldReturn400WhenCountryUnknown() {
            var country = jdbcAggregateTemplate.insert(testCountry());
            var cap = jdbcAggregateTemplate.insert(testCap(country));
            var updateCap = new UpdateCap()
                .name("Updated Cap")
                .description("Updated description")
                .amount(10)
                .country("XX");

            assertThatThrownBy(() -> capApi.updateCap(cap.id().toString(), updateCap))
                .isInstanceOfSatisfying(ApiException.class, exception -> {
                    assertThat(exception.getCode()).isEqualTo(400);
                    var problem = parseProblem(exception);
                    assertThat(problem.getTitle()).isEqualTo("Bad Request");
                    assertThat(problem.getStatus()).isEqualTo(400);
                    assertThat(problem.getDetail()).isEqualTo("Unknown country XX");
                });
        }
    }

    @Nested
    class Delete {
        @Test
        void shouldDeleteExistingCap() throws ApiException {
            var country = jdbcAggregateTemplate.insert(testCountry());
            var cap = jdbcAggregateTemplate.insert(testCap(country));

            capApi.deleteCapById(cap.id().toString());

            assertThat(capApi.findCaps(null)).isEmpty();
        }

        @Test
        void shouldReturn404WhenDeletingNonExistentCap() {
            var randomId = CapId.randomCapId();

            assertThatThrownBy(() -> capApi.deleteCapById(randomId.value()))
                .isInstanceOfSatisfying(ApiException.class, exception -> {
                    assertThat(exception.getCode()).isEqualTo(404);
                    var problem = parseProblem(exception);
                    assertThat(problem.getTitle()).isEqualTo("Not Found");
                    assertThat(problem.getStatus()).isEqualTo(404);
                    assertThat(problem.getDetail()).isEqualTo("Cap %s not found".formatted(randomId.value()));
                    assertThat(problem.getInstance()).isEqualTo("/caps/%s".formatted(randomId.value()));
                });
        }
    }

    private CapEntity testCap(CountryEntity country) {
        return new CapEntity(CapId.randomCapId().toUuid(), "Belgian Cap", "This is a Belgian Cap", 5, country.code());
    }

    private CountryEntity testCountry() {
        return testCountry("BE");
    }

    private CountryEntity testCountry(String code) {
        return new CountryEntity(UUID.randomUUID(), "Belgium", code);
    }
}
