package org.theoriok.inventory.persistence.entities;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.annotations.GenericGenerator;

import java.io.Serializable;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Table(name = "book")
@Entity
public class BookEntity implements Serializable {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(updatable = false, nullable = false, columnDefinition = "uuid DEFAULT gen_random_uuid()")
    private UUID id;

    @Column(nullable = false, unique = true)
    private String businessId;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String author;
    @Column(columnDefinition = "text")
    private String description;

    public BookEntity() {
    }

    public BookEntity(String businessId, String title, String author, String description) {
        this.businessId = businessId;
        this.description = description;
        this.title = title;
        this.author = author;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getBusinessId() {
        return businessId;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof BookEntity bookEntity)) {
            return false;
        }

        return new EqualsBuilder()
            .append(title, bookEntity.title)
            .append(author, bookEntity.author)
            .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
            .append(title)
            .append(author)
            .toHashCode();
    }
}
