package ru.netology;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BasketBuilder {
    private final PurchaseStatsProvider statsProvider;

    public BasketBuilder(PurchaseStatsProvider statsProvider) {
        this.statsProvider = statsProvider;
    }

    public List<Product> buildBasket(int topN) {
        return statsProvider.getPurchaseStats().entrySet().stream()
                .sorted((a, b) -> b.getValue() - a.getValue())
                .limit(topN)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }
}




