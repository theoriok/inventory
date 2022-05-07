package org.theoriok.inventory.persistence.mappers;

import java.util.Collection;
import java.util.stream.Collectors;

public interface EntityMapper<D, E> {
    E toEntity(D domainObject);

    D toDomainObject(E entity);

    default Collection<E> toEntities(Collection<D> domainObjects) {
        return domainObjects.stream()
            .map(this::toEntity)
            .collect(Collectors.toList());
    }

    default Collection<D> toDomainObjects(Collection<E> entities) {
        return entities.stream()
            .map(this::toDomainObject)
            .collect(Collectors.toList());
    }
}
