package org.theoriok.inventory.web.adapters;

import static java.util.Collections.emptyList;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequestMapping("/books")
public class BookController {

    @GetMapping
    public ResponseEntity<Collection<Object>> findBooks() {
        return ResponseEntity.ok(emptyList());
    }


}
