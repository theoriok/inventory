package org.theoriok.inventory.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CapDto(
    @JsonProperty("business_id") String businessId,
    @JsonProperty("name") String name,
    @JsonProperty("description") String description,
    @JsonProperty("amount") int amount,
    @JsonProperty("country") CountryDto country
) {
}