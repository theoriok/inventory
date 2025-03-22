package org.theoriok.inventory.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;

public record UpsertCapDto(
    @JsonProperty("business_id") @NotEmpty String businessId,
    @JsonProperty("name") String name,
    @JsonProperty("description") String description,
    @JsonProperty("amount") int amount,
    @JsonProperty("country") @NotEmpty String country
) {
}