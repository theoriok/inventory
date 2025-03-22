package org.theoriok.inventory.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CountryDto(@JsonProperty("name") String name, @JsonProperty("code") String code) {
}
