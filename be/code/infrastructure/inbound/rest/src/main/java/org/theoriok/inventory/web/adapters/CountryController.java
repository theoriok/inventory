package org.theoriok.inventory.web.adapters;

import static org.springframework.http.HttpStatus.NOT_FOUND;

import io.micrometer.core.annotation.Timed;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.theoriok.inventory.domain.Country;
import org.theoriok.inventory.query.FindCountries;
import org.theoriok.inventory.web.dto.CountryDto;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/countries")
@Timed
@CrossOrigin(origins = "http://localhost:3000")
public class CountryController {
    private final FindCountries findCountries;

    public CountryController(FindCountries findCountries) {
        this.findCountries = findCountries;
    }

    @GetMapping
    public ResponseEntity<Collection<CountryDto>> findCountries() {
        return ResponseEntity.ok(toCountryDtos(findCountries.findAll()));
    }

    private List<CountryDto> toCountryDtos(List<Country> countriesResponse) {
        return countriesResponse.stream()
            .map(this::toCountryDto)
            .toList();
    }

    @GetMapping("/{code}")
    public ResponseEntity<CountryDto> findCountryByCode(@PathVariable(name = "code") String code) {
        return findCountries.findByCode(code)
            .map(this::toCountryDto)
            .map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.of(ProblemDetail.forStatus(NOT_FOUND)).build());
    }

    private CountryDto toCountryDto(Country country) {
        return new CountryDto(country.name(), country.code());
    }
}
