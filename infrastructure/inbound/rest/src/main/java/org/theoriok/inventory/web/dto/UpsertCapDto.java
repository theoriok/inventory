package org.theoriok.inventory.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotEmpty;

public class UpsertCapDto {
    @NotEmpty
    @JsonProperty("business_id")
    private String businessId;
    @JsonProperty("name")
    private String name;
    @JsonProperty("description")
    private String description;
    @JsonProperty("amount")
    private int amount;
    @NotEmpty
    @JsonProperty("country")
    private String country;

    public UpsertCapDto() {
    }

    public UpsertCapDto(String businessId, String name, String description, int amount, String country) {
        this.businessId = businessId;
        this.name = name;
        this.description = description;
        this.amount = amount;
        this.country = country;
    }

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
