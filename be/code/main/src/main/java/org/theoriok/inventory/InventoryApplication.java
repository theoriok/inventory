package org.theoriok.inventory;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "org.theoriok.inventory")
public class InventoryApplication {
    static void main(String[] args) {
        SpringApplication.run(InventoryApplication.class, args);
    }
}
