package org.theoriok.inventory.persistence.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Table("country")
public record CountryEntity(
    @Id UUID id,
    String name,
    String code
) {}
