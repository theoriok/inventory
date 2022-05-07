package org.theoriok.inventory.port;

import org.theoriok.inventory.domain.Country;

public interface PersistCountryPort {
    Country findByCode(String code);
}
