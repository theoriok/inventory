package org.theoriok.inventory.persistence.mappers;

import java.util.Collection;

public interface EntityMapper<D, E> {
    E toEntity(D domainObject);

    D toDomainObject(E entity);

    default Collection<D> toDomainObjects(Collection<E> entities) {
        return entities.stream()
            .map(this::toDomainObject)
            .toList();
    }
}
