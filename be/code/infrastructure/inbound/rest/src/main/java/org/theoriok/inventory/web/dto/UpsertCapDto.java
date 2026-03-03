package org.theoriok.inventory.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record UpsertCapDto(
    @JsonProperty("business_id") @NotBlank @Size(max = 255) String businessId,
    @JsonProperty("name") @NotBlank @Size(max = 255) String name,
    @JsonProperty("description") @NotBlank @Size(max = 5000) String description,
    @JsonProperty("amount") @NotNull @Positive Integer amount,
    @JsonProperty("country") @NotBlank @Size(max = 10) String country
) {
}
