package org.theoriok.inventory.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BookDto {

    @JsonProperty("business_id")
    private String businessId;
    @JsonProperty("title")
    private String title;
    @JsonProperty("author")
    private String author;
    @JsonProperty("description")
    private String description;

    public BookDto() {
    }

    public BookDto(String businessId, String title, String author, String description) {
        this.businessId = businessId;
        this.title = title;
        this.author = author;
        this.description = description;
    }
}
