package org.theoriok.inventory.mappers;

import java.util.Collection;

public interface CommandMapper<DO, Request, ListResponse, SingleResponse> {
    SingleResponse toSingleResponse(DO domainObject);

    ListResponse toListResponse(Collection<DO> domainObjects);

    DO toDomainObject(Request dto);
}
