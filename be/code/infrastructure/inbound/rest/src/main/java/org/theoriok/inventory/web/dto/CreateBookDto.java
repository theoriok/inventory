package org.theoriok.inventory.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateBookDto(
    @JsonProperty("title") @NotBlank @Size(max = 255) String title,
    @JsonProperty("author") @NotBlank @Size(max = 255) String author,
    @JsonProperty("description") @NotBlank @Size(max = 5000) String description
) {
}
