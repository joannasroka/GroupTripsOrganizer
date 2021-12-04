package com.sroka.grouptripsorganizer.entity.bill;

import java.util.List;

public enum Currency {
    USD,
    EUR,
    GBP,
    PLN,
    AUD,
    RUB,
    BRL,
    RON,
    HRK,
    SEK,
    DKK,
    CZK,
    JEN,
    UAH,
    HUF;

    public static final List<Currency> CURRENCIES = List.of(values());
}
