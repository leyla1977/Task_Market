package ru.netology;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class BasketHelper {
    public static void askToAddToBasket(Scanner scanner, Basket userBasket, List<Product> products) {
        System.out.print("Хотите добавить товар в корзину? (да/нет): ");
        String response = scanner.nextLine().trim().toLowerCase();

        if (response.equals("да")) {
            System.out.print("Введите ID товара для добавления: ");
            try {
                int id = Integer.parseInt(scanner.nextLine());
                Optional<Product> selectedProduct = products.stream()
                        .filter(product -> product.getId() == id)
                        .findFirst();

                selectedProduct.ifPresentOrElse(
                        product -> {
                            userBasket.addProduct(product);
                            System.out.println("✅ Товар \"" + product.getProductName() + "\" добавлен в корзину.");
                        },
                        () -> System.out.println("❌ Товар с ID " + id + " не найден среди отфильтрованных.")
                );

            } catch (NumberFormatException e) {
                System.out.println("⚠ Некорректный ID.");
            }
        }
    }
}

