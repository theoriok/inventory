package org.theoriok.inventory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.theoriok.inventory.client.ApiException;
import org.theoriok.inventory.persistence.entities.CountryEntity;

import java.util.UUID;

class CountryContractTest extends ContractTest {

    @Nested
    class Find {
        @Test
        void shouldReturnEmptyListWhenNoCountriesExist() throws ApiException {
            var countries = countryApi.findCountries();

            assertThat(countries).isEmpty();
        }

        @Test
        void shouldReturnCountryWhenCountryExists() throws ApiException {
            jdbcAggregateTemplate.insert(testCountry());

            var countries = countryApi.findCountries();

            assertThat(countries).singleElement().satisfies(country -> {
                assertThat(country.getName()).isEqualTo("Belgium");
                assertThat(country.getCode()).isEqualTo("BE");
            });
        }

        @Test
        void shouldReturnCountryByCode() throws ApiException {
            jdbcAggregateTemplate.insert(testCountry());

            var result = countryApi.findCountryByCode("BE");

            assertThat(result.getName()).isEqualTo("Belgium");
            assertThat(result.getCode()).isEqualTo("BE");
        }

        @Test
        void shouldReturn404WhenCountryNotFound() {
            assertThatThrownBy(() -> countryApi.findCountryByCode("XX"))
                .isInstanceOfSatisfying(ApiException.class, exception -> {
                    assertThat(exception.getCode()).isEqualTo(404);
                    var problem = parseProblem(exception);
                    assertThat(problem.getTitle()).isEqualTo("Not Found");
                    assertThat(problem.getStatus()).isEqualTo(404);
                    assertThat(problem.getDetail()).isEqualTo("Country XX not found");
                    assertThat(problem.getInstance()).isEqualTo("/countries/XX");
                });
        }
    }

    private CountryEntity testCountry() {
        return new CountryEntity(UUID.randomUUID(), "Belgium", "BE");
    }
}
