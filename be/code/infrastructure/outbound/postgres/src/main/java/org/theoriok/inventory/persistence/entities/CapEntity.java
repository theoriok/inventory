package org.theoriok.inventory.persistence.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Table("cap")
public record CapEntity(
    @Id UUID id,
    String name,
    String description,
    int amount,
    String countryCode
) {}
