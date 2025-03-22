package org.theoriok.inventory.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record BookDto(
    @JsonProperty("business_id") String businessId,
    @JsonProperty("title") String title,
    @JsonProperty("author") String author,
    @JsonProperty("description") String description
) {
}