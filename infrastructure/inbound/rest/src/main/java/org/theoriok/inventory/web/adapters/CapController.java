package org.theoriok.inventory.web.adapters;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.theoriok.inventory.command.UpsertCap;
import org.theoriok.inventory.query.FindCaps;
import org.theoriok.inventory.web.dto.CapDto;
import org.theoriok.inventory.web.dto.CountryDto;
import org.theoriok.inventory.web.dto.UpsertCapDto;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/caps")
public class CapController {
    private final UpsertCap upsertCap;
    private final FindCaps findCaps;

    public CapController(UpsertCap upsertCap, FindCaps findCaps) {
        this.upsertCap = upsertCap;
        this.findCaps = findCaps;
    }

    @GetMapping
    public ResponseEntity<Collection<CapDto>> allCaps() {
        var capsResponse = findCaps.findAll();
        return ResponseEntity.ok(toCapDtos(capsResponse));
    }

    private List<CapDto> toCapDtos(FindCaps.ListResponse capsResponse) {
        return capsResponse.caps().stream()
            .map(this::toCapDto)
            .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CapDto> findCapById(@PathVariable String id) {
        return findCaps.findById(id)
            .map(FindCaps.SingleResponse::cap)
            .map(this::toCapDto)
            .map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping
    public ResponseEntity<?> upsertCap(@RequestBody UpsertCapDto capDto) {
        upsertCap.upsert(toUpsertRequest(capDto));
        return ResponseEntity.noContent().build();
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
            dto.getBusinessId(),
            dto.getName(),
            dto.getDescription(),
            dto.getAmount(),
            dto.getCountry()
        );
    }
}
