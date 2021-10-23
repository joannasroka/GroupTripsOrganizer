package com.sroka.grouptripsorganizer.entity.bill;

import java.util.List;

public enum Currency {
    USD,
    EUR,
    GBP,
    PLN;

    public static final List<Currency> CURRENCIES = List.of(values());
}
