package org.theoriok.inventory.domain;

import org.theoriok.inventory.CountryCode;
import org.theoriok.inventory.Name;

public record Country(Name name, CountryCode code) {
}
