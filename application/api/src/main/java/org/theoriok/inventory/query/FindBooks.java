package org.theoriok.inventory.query;

import java.util.List;

public interface FindBooks {
    ListResponse findAll();

    record ListResponse(List<Book> books) {
    }

    record Book(String businessId, String title, String author, String description) {
    }
}
