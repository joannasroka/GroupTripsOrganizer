package com.sroka.grouptripsorganizer.entity.bill;

import java.util.List;

public enum BillCategory {
    TRANSPORT,
    ACCOMMODATION,
    FOOD_AND_DRINK,
    SOUVENIRS,
    TICKETS,
    ENTERTAINMENT,
    INSURANCE,
    SHOPPING,
    HEALTH,
    OTHER;

    public static final List<BillCategory> BILL_CATEGORIES = List.of(values());
}
