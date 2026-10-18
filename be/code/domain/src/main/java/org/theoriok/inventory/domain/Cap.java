package org.theoriok.inventory.domain;

import org.theoriok.inventory.Amount;
import org.theoriok.inventory.CapId;
import org.theoriok.inventory.Description;
import org.theoriok.inventory.Name;

public record Cap(CapId id, Name name, Description description, Amount amount, Country country) {
    public static Cap create(CapId id, Name name, Description description, Amount amount, Country country) {
        return new Cap(id, name, description, amount, country);
    }

    public Cap update(Name name, Description description, Amount amount, Country country) {
        return new Cap(this.id, name, description, amount, country);
    }
}
