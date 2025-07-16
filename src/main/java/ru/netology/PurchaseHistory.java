package ru.netology;

import java.util.HashMap;
import java.util.Map;

public class PurchaseHistory implements PurchaseStatsProvider {
    private final Map<Product, Integer> purchases = new HashMap<>();

    public void addPurchase(Product product) {
        purchases.put(product, purchases.getOrDefault(product, 0) + 1);
    }

    @Override
    public Map<Product, Integer> getPurchaseStats() {
        return purchases;
    }
}
