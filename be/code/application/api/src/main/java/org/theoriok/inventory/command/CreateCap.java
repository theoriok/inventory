package org.theoriok.inventory.command;

import org.theoriok.inventory.Amount;
import org.theoriok.inventory.CapId;
import org.theoriok.inventory.CountryCode;
import org.theoriok.inventory.Description;
import org.theoriok.inventory.Name;
import org.theoriok.inventory.domain.Cap;

public interface CreateCap {
    Result create(Request request);

    record Request(CapId id, Name name, Description description, Amount amount, CountryCode country) {
    }

    sealed interface Result {
        record Created(Cap cap) implements Result {
        }

        record UnknownCountry(CountryCode country) implements Result {
        }
    }
}
