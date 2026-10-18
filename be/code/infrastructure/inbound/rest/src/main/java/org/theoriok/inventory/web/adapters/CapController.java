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
import org.theoriok.inventory.Amount;
import org.theoriok.inventory.CapId;
import org.theoriok.inventory.CountryCode;
import org.theoriok.inventory.Description;
import org.theoriok.inventory.Name;
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
        var countryCode = country == null ? null : new CountryCode(country);
        var capsResponse = findCaps.findAll(new FindCaps.Request(countryCode));
        return ResponseEntity.ok(toCapDtos(capsResponse));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CapDto> findCapById(@PathVariable(name = "id") String id) {
        return findCaps.findById(new CapId(id))
            .map(this::toCapDto)
            .map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.of(ProblemDetail.forStatusAndDetail(NOT_FOUND, "Cap %s not found".formatted(id))).build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCapById(@PathVariable(name = "id") String id) {
        return switch (deleteCap.delete(new CapId(id))) {
            case DeleteCap.Result.Deleted() -> ResponseEntity.ok().build();
            case DeleteCap.Result.NotFound(var capId) -> ResponseEntity.of(ProblemDetail.forStatusAndDetail(NOT_FOUND, "Cap %s not found".formatted(capId.value()))).build();
        };
    }

    @PostMapping
    public ResponseEntity<?> createCap(@Valid @RequestBody CreateCapDto capDto) {
        return switch (createCap.create(toCreateRequest(capDto))) {
            case CreateCap.Result.Created(var cap) -> ResponseEntity.status(HttpStatus.CREATED).body(toCapDto(cap));
            case CreateCap.Result.UnknownCountry(var country) -> ResponseEntity.of(ProblemDetail.forStatusAndDetail(BAD_REQUEST, "Unknown country %s".formatted(country.value()))).build();
        };
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCap(@PathVariable(name = "id") String id, @Valid @RequestBody UpdateCapDto capDto) {
        return switch (updateCap.update(toUpdateRequest(id, capDto))) {
            case UpdateCap.Result.Updated() -> ResponseEntity.noContent().build();
            case UpdateCap.Result.NotFound(var capId) -> ResponseEntity.of(ProblemDetail.forStatusAndDetail(NOT_FOUND, "Cap %s not found".formatted(capId.value()))).build();
            case UpdateCap.Result.UnknownCountry(var country) -> ResponseEntity.of(ProblemDetail.forStatusAndDetail(BAD_REQUEST, "Unknown country %s".formatted(country.value()))).build();
        };
    }

    private CreateCap.Request toCreateRequest(CreateCapDto capDto) {
        return new CreateCap.Request(
            CapId.randomCapId(),
            new Name(capDto.name()),
            new Description(capDto.description()),
            new Amount(capDto.amount()),
            new CountryCode(capDto.country())
        );
    }

    private UpdateCap.Request toUpdateRequest(String id, UpdateCapDto capDto) {
        return new UpdateCap.Request(
            new CapId(id),
            new Name(capDto.name()),
            new Description(capDto.description()),
            new Amount(capDto.amount()),
            new CountryCode(capDto.country())
        );
    }

    private List<CapDto> toCapDtos(List<Cap> capsResponse) {
        return capsResponse.stream()
            .map(this::toCapDto)
            .toList();
    }

    private CapDto toCapDto(Cap cap) {
        return new CapDto(
            cap.id().value(),
            cap.name().value(),
            cap.description().value(),
            cap.amount().value(),
            toCountryDto(cap.country())
        );
    }

    private CountryDto toCountryDto(Country domainObject) {
        return new CountryDto(
            domainObject.name().value(),
            domainObject.code().value()
        );
    }
}
