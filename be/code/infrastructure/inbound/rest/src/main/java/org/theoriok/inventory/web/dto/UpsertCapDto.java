package org.theoriok.inventory.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record UpsertCapDto(
    @JsonProperty("business_id") @NotBlank String businessId,
    @JsonProperty("name") @NotBlank String name,
    @JsonProperty("description") @NotBlank String description,
    @JsonProperty("amount") @Positive int amount,
    @JsonProperty("country") @NotNull String country
) {
}
