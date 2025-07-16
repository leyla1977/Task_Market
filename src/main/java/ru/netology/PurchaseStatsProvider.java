package ru.netology;

import java.util.Map;

public interface PurchaseStatsProvider {
    Map<Product, Integer> getPurchaseStats();
}