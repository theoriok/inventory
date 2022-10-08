package org.theoriok.inventory.web.adapters;

import io.micrometer.core.annotation.Timed;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
        var countriesResponse = findCountries.findAll();
        return ResponseEntity.ok(toCountryDtos(countriesResponse));
    }

    private List<CountryDto> toCountryDtos(FindCountries.ListResponse countriesResponse) {
        return countriesResponse.countries().stream()
            .map(this::toCountryDto)
            .toList();
    }

    @GetMapping("/{code}")
    public ResponseEntity<CountryDto> findCountryByCode(@PathVariable String code) {
        return findCountries.findByCode(code)
            .map(FindCountries.SingleResponse::country)
            .map(this::toCountryDto)
            .map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.notFound().build());
    }

    private CountryDto toCountryDto(FindCountries.Country domainObject) {
        return new CountryDto(
            domainObject.name(),
            domainObject.code()
        );
    }
}
