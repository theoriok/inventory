package org.theoriok.inventory.persistence.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.id.uuid.UuidGenerator;

import java.io.Serializable;
import java.util.UUID;

@Table(name = "country")
@Entity
public class CountryEntity implements Serializable {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", type = UuidGenerator.class)
    @Column(updatable = false, nullable = false, columnDefinition = "uuid DEFAULT gen_random_uuid()")
    private UUID id;

    @Column(nullable = false)
    private String name;
    @Column(nullable = false, unique = true)
    private String code;

    public CountryEntity() {
    }

    public CountryEntity(String name, String code) {
        this.name = name;
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof CountryEntity countryEntity)) {
            return false;
        }

        return new EqualsBuilder().append(code, countryEntity.code).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(code).toHashCode();
    }
}
