package org.theoriok.inventory.persistence.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.io.Serializable;
import java.util.UUID;

@Table(name = "book")
@Entity
public class BookEntity implements Serializable {
    @Id
    @Column(updatable = false, nullable = false)
    private UUID id;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String author;
    @Column(columnDefinition = "text")
    private String description;

    public BookEntity() {
    }

    public BookEntity(UUID id, String title, String author, String description) {
        this.id = id;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
            .append(description, bookEntity.description)
            .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
            .append(title)
            .append(author)
            .append(description)
            .toHashCode();
    }
}
