package org.theoriok.inventory.persistence.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Table("book")
public record BookEntity(
    @Id UUID id,
    String title,
    String author,
    String description
) {}
