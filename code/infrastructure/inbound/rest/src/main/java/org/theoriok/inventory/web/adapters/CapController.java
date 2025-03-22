package org.theoriok.inventory.web.adapters;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.theoriok.inventory.command.DeleteCap;
import org.theoriok.inventory.command.UpsertCap;
import org.theoriok.inventory.query.FindCaps;
import org.theoriok.inventory.web.dto.CapDto;
import org.theoriok.inventory.web.dto.CountryDto;
import org.theoriok.inventory.web.dto.UpsertCapDto;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/caps")
@Timed
@CrossOrigin(origins = "http://localhost:3000")
public class CapController {
    private final UpsertCap upsertCap;
    private final FindCaps findCaps;
    private final DeleteCap deleteCap;

    public CapController(UpsertCap upsertCap, FindCaps findCaps, DeleteCap deleteCap) {
        this.upsertCap = upsertCap;
        this.findCaps = findCaps;
        this.deleteCap = deleteCap;
    }

    @GetMapping
    public ResponseEntity<Collection<CapDto>> findCaps(@RequestParam(required = false, name = "country") String country) {
        var capsResponse = findCaps.findAll(new FindCaps.Request(country));
        return ResponseEntity.ok(toCapDtos(capsResponse));
    }

    private List<CapDto> toCapDtos(FindCaps.ListResponse capsResponse) {
        return capsResponse.caps().stream()
            .map(this::toCapDto)
            .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CapDto> findCapById(@PathVariable(name = "id") String id) {
        return findCaps.findById(id)
            .map(FindCaps.SingleResponse::cap)
            .map(this::toCapDto)
            .map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.of(ProblemDetail.forStatus(NOT_FOUND)).build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCapById(@PathVariable(name = "id") String id) {
        return switch (deleteCap.delete(id)) {
            case DELETED -> ResponseEntity.ok().build();
            case NOT_FOUND -> ResponseEntity.of(ProblemDetail.forStatus(NOT_FOUND)).build();
        };
    }

    @PutMapping
    public ResponseEntity<?> upsertCap(@RequestBody UpsertCapDto capDto) {
        return switch (upsertCap.upsert(toUpsertRequest(capDto))) {
            case UPSERTED -> ResponseEntity.noContent().build();
            case UNKNOWN_COUNTRY -> ResponseEntity.of(ProblemDetail.forStatusAndDetail(BAD_REQUEST, "Unknown country %s".formatted(capDto.country()))).build();
        };
    }

    private CapDto toCapDto(FindCaps.Cap domainObject) {
        return new CapDto(
            domainObject.businessId(),
            domainObject.name(),
            domainObject.description(),
            domainObject.amount(),
            toCountryDto(domainObject.country())
        );
    }

    private CountryDto toCountryDto(FindCaps.Country domainObject) {
        return new CountryDto(
            domainObject.name(),
            domainObject.code()
        );
    }

    private UpsertCap.Request toUpsertRequest(UpsertCapDto dto) {
        return new UpsertCap.Request(
            dto.businessId(),
            dto.name(),
            dto.description(),
            dto.amount(),
            dto.country()
        );
    }
}
