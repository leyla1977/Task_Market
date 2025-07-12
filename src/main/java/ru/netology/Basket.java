package ru.netology;


import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Basket {
    // Ключ — продукт, значение — количество в корзине
    private final Map<Product, Integer> items = new HashMap<>();

    // Добавить товар в корзину (увеличить количество на 1)
    public void addProduct(Product product) {
        items.put(product, items.getOrDefault(product, 0) + 1);
    }

    // Удалить один товар из корзины (уменьшить количество на 1, если 0 — удалить)
    public void removeProduct(Product product) {
        if (!items.containsKey(product)) {
            System.out.println("Товар в корзине отсутствует.");
            return;
        }
        int count = items.get(product);
        if (count <= 1) {
            items.remove(product);
        } else {
            items.put(product, count - 1);
        }
    }

    // Получить множество товаров в корзине
    public Set<Product> getProducts() {
        return items.keySet();
    }

    // Получить количество товара
    public int getQuantity(Product product) {
        return items.getOrDefault(product, 0);
    }

    // Вывести содержимое корзины с количеством
    public void printBasket() {
        if (items.isEmpty()) {
            System.out.println("Корзина пуста.");
            return;
        }
        System.out.println("🛒 Товары в корзине:");
        for (Map.Entry<Product, Integer> entry : items.entrySet()) {
            Product p = entry.getKey();
            int qty = entry.getValue();
            // Вызовем метод вывода для корзины с конкретным количеством
            System.out.println("ID: " + p.getId() + " | " + p.toStringForBasket(qty));
        }
        System.out.printf("💰 Общая сумма: %.2f руб.%n", getTotalAmount());
    }



    // Общая сумма товаров в корзине
    public double getTotalAmount() {
        double total = 0;
        for (Map.Entry<Product, Integer> entry : items.entrySet()) {
            total += entry.getKey().getPrice() * entry.getValue();
        }
        return total;
    }
}

