package ru.netology;


import java.util.List;
import java.util.stream.Collectors;

public class ProductSearchService {
    public List<Product> search(List<Product> products, String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return List.of(); // ничего не ищем, если запрос пустой
        }

        return products.stream()
                .filter(p -> p.isMatches(keyword))
                .collect(Collectors.toList());
    }
}

