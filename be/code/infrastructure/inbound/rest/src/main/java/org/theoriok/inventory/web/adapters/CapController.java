package org.theoriok.inventory.web.adapters;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import io.micrometer.core.annotation.Timed;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.theoriok.inventory.CapId;
import org.theoriok.inventory.command.CreateCap;
import org.theoriok.inventory.command.DeleteCap;
import org.theoriok.inventory.command.UpdateCap;
import org.theoriok.inventory.domain.Cap;
import org.theoriok.inventory.domain.Country;
import org.theoriok.inventory.query.FindCaps;
import org.theoriok.inventory.web.dto.CapDto;
import org.theoriok.inventory.web.dto.CountryDto;
import org.theoriok.inventory.web.dto.CreateCapDto;
import org.theoriok.inventory.web.dto.UpdateCapDto;

import java.util.List;

@RestController
@RequestMapping("/caps")
@Timed
public class CapController {
    private final FindCaps findCaps;
    private final CreateCap createCap;
    private final UpdateCap updateCap;
    private final DeleteCap deleteCap;

    public CapController(FindCaps findCaps, CreateCap createCap, UpdateCap updateCap, DeleteCap deleteCap) {
        this.findCaps = findCaps;
        this.createCap = createCap;
        this.updateCap = updateCap;
        this.deleteCap = deleteCap;
    }

    @GetMapping
    public ResponseEntity<List<CapDto>> findCaps(@RequestParam(required = false, name = "country") String country) {
        var capsResponse = findCaps.findAll(new FindCaps.Request(country));
        return ResponseEntity.ok(toCapDtos(capsResponse));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CapDto> findCapById(@PathVariable(name = "id") String id) {
        return findCaps.findById(new CapId(id))
            .map(this::toCapDto)
            .map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.of(ProblemDetail.forStatus(NOT_FOUND)).build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCapById(@PathVariable(name = "id") String id) {
        return switch (deleteCap.delete(new CapId(id))) {
            case DELETED -> ResponseEntity.ok().build();
            case NOT_FOUND -> ResponseEntity.of(ProblemDetail.forStatus(NOT_FOUND)).build();
        };
    }

    @PostMapping
    public ResponseEntity<?> createCap(@Valid @RequestBody CreateCapDto capDto) {
        return switch (createCap.create(toCreateRequest(capDto))) {
            case CreateCap.Result.Created(var cap) -> ResponseEntity.status(HttpStatus.CREATED).body(toCapDto(cap));
            case CreateCap.Result.UnknownCountry() -> ResponseEntity.of(ProblemDetail.forStatusAndDetail(BAD_REQUEST, "Unknown country %s".formatted(capDto.country()))).build();
        };
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCap(@PathVariable(name = "id") String id, @Valid @RequestBody UpdateCapDto capDto) {
        return switch (updateCap.update(toUpdateRequest(id, capDto))) {
            case UPDATED -> ResponseEntity.noContent().build();
            case NOT_FOUND -> ResponseEntity.of(ProblemDetail.forStatus(NOT_FOUND)).build();
            case UNKNOWN_COUNTRY -> ResponseEntity.of(ProblemDetail.forStatusAndDetail(BAD_REQUEST, "Unknown country %s".formatted(capDto.country()))).build();
        };
    }

    private CreateCap.Request toCreateRequest(CreateCapDto capDto) {
        return new CreateCap.Request(
            CapId.randomCapId(),
            capDto.name(),
            capDto.description(),
            capDto.amount(),
            capDto.country()
        );
    }

    private UpdateCap.Request toUpdateRequest(String id, UpdateCapDto capDto) {
        return new UpdateCap.Request(
            new CapId(id),
            capDto.name(),
            capDto.description(),
            capDto.amount(),
            capDto.country()
        );
    }

    private List<CapDto> toCapDtos(List<Cap> capsResponse) {
        return capsResponse.stream()
            .map(this::toCapDto)
            .toList();
    }

    private CapDto toCapDto(Cap domainObject) {
        return new CapDto(
            domainObject.id().value(),
            domainObject.name(),
            domainObject.description(),
            domainObject.amount(),
            toCountryDto(domainObject.country())
        );
    }

    private CountryDto toCountryDto(Country domainObject) {
        return new CountryDto(
            domainObject.name(),
            domainObject.code()
        );
    }
}
