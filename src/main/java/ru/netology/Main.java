package ru.netology;

import java.util.*;
import java.util.stream.Collectors;

public class Main {
    private static int nextOrderId = 1;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Производители
        Manufacturer gmz = new Manufacturer("Городецкий молокозавод", "Россия");
        Manufacturer shz = new Manufacturer("Сормовский хлебзавод", "Россия");
        Manufacturer spf = new Manufacturer("Сеймовская птицефабрика", "Россия");
        Manufacturer bl = new Manufacturer("Брест-литовский", "Беларусь");
        Manufacturer kmk = new Manufacturer("Краснодарский масложирокомбинат", "Россия");

        // Рейтинги
        Rating milkRating = new Rating(5, 4, 5, 5);
        Rating breadRating = new Rating(4, 5, 5, 3);
        Rating eggsRating = new Rating(5, 5, 4, 4);
        Rating cheeseRating = new Rating(5, 4, 4, 4);
        Rating juiceRating = new Rating(5, 5, 5, 4);

        // Продукты
        List<Product> allProducts = List.of(
                new Product(1, "Молоко", gmz, 75, 150, milkRating),
                new Product(2, "Хлеб", shz, 50, 200, breadRating),
                new Product(3, "Яйца", spf, 100, 50, eggsRating),
                new Product(4, "Сыр", bl, 1000, 100, cheeseRating),
                new Product(5, "Масло растительное", kmk, 150, 50, juiceRating)
        );

        List<Product> userBasket = new ArrayList<>();
        List<Order> orders = new ArrayList<>();

        while (true) {
            System.out.println("\n=== МЕНЮ ===");
            System.out.println("1. Просмотреть все продукты");
            System.out.println("2. Поиск по ключевому слову");
            System.out.println("3. Поиск по производителю");
            System.out.println("4. Просмотреть корзину");
            System.out.println("5. Оформить заказ");
            System.out.println("6. Трекинг заказа");
            System.out.println("7. Повторить заказ");
            System.out.println("8. Возврат заказа");
            System.out.println("0. Выход");

            System.out.print("Выберите действие: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    allProducts.forEach(System.out::println);
                    System.out.print("Введите ID товаров через запятую, чтобы добавить в корзину, или 0 для выхода: ");
                    String input = scanner.nextLine();
                    if (!input.equals("0")) {
                        String[] ids = input.split(",");
                        for (String idStr : ids) {
                            try {
                                int id = Integer.parseInt(idStr.trim());
                                allProducts.stream()
                                        .filter(p -> p.getId() == id)
                                        .findFirst()
                                        .ifPresentOrElse(
                                                product -> {
                                                    userBasket.add(product);
                                                    System.out.println("✅ Товар \"" + product.getProductName() + "\" добавлен в корзину.");
                                                },
                                                () -> System.out.println("❌ Товар с ID " + id + " не найден.")
                                        );
                            } catch (NumberFormatException e) {
                                System.out.println("⚠ Некорректный ID: " + idStr);
                            }
                        }
                    }
                    break;


                case "2":
                    System.out.print("Введите ключевое слово: ");
                    String keyword = scanner.nextLine();

                    List<Product> matched = allProducts.stream()
                            .filter(p -> p.isMatches(keyword))
                            .collect(Collectors.toList());

                    if (matched.isEmpty()) {
                        System.out.println("Товары не найдены.");
                        break;
                    }

                    System.out.println("🔍 Найденные товары:");
                    matched.forEach(System.out::println);

                    System.out.println("Уточнить поиск по цене или рейтингу? (1 - цена, 2 - рейтинг, 0 - нет): ");
                    int refine = Integer.parseInt(scanner.nextLine());
                    if (refine == 1) {
                        System.out.print("Введите макс. цену: ");
                        double maxPrice = Double.parseDouble(scanner.nextLine());
                        matched = matched.stream()
                                .filter(p -> p.getPrice() <= maxPrice)
                                .collect(Collectors.toList());
                    } else if (refine == 2) {
                        System.out.print("Введите мин. рейтинг: ");
                        double minRating = Double.parseDouble(scanner.nextLine());
                        matched = matched.stream()
                                .filter(p -> p.getRating().calculateAverage() >= minRating)
                                .collect(Collectors.toList());
                    }

                    if (matched.isEmpty()) {
                        System.out.println("После фильтрации ничего не найдено.");
                    } else {
                        matched.forEach(System.out::println);
                        askToAddToBasket(scanner, userBasket, matched);
                    }
                    break;

                case "3":
                    System.out.print("Введите имя производителя: ");
                    String manufacturerName = scanner.nextLine().toLowerCase();
                    List<Product> byManufacturer = allProducts.stream()
                            .filter(p -> p.getManufacturer().getManufacturerName().toLowerCase().contains(manufacturerName))
                            .collect(Collectors.toList());

                    if (byManufacturer.isEmpty()) {
                        System.out.println("Товары не найдены.");
                    } else {
                        byManufacturer.forEach(System.out::println);
                        askToAddToBasket(scanner, userBasket, byManufacturer);
                    }
                    break;

                case "4":
                    if (userBasket.isEmpty()) {
                        System.out.println("🛒 Корзина пуста.");
                    } else {
                        System.out.println("🛒 Товары в корзине:");

                        // Группируем товары в корзине по продукту и считаем количество каждого
                        Map<Product, Long> productCount = userBasket.stream()
                                .collect(Collectors.groupingBy(p -> p, Collectors.counting()));

                        productCount.forEach((product, count) -> {
                            System.out.println("ID: " + product.getId() + " | " + product.getProductName() + " - "
                                    + product.getPrice() + " (" + product.getManufacturer().toString() + ") Кол-во: " + count + " ед.");
                        });

                        double total = userBasket.stream().mapToDouble(Product::getPrice).sum();
                        System.out.println("💰 Общая сумма: " + total + " руб.");

                        while (true) {
                            System.out.println("\nВыберите действие:");
                            System.out.println("1. ✅ Оформить заказ");
                            System.out.println("2. ➕ Добавить товар в корзину");
                            System.out.println("3. ❌ Удалить товар из корзины");
                            System.out.println("0. 🔙 Вернуться в меню");

                            String subChoice = scanner.nextLine();
                            switch (subChoice) {
                                case "1":
                                    Order newOrder = new Order(userBasket);
                                    orders.add(newOrder);
                                    System.out.println("📦 Заказ оформлен! Номер заказа: " + newOrder.getOrderId());
                                    userBasket.clear();
                                    break;
                                case "2":
                                    System.out.println("📃 Доступные товары:");
                                    allProducts.forEach(p -> System.out.println("ID: " + p.getId() + " | " + p.getProductName()
                                            + " - " + p.getPrice() + " (" + p.getManufacturer().toString() + ") Кол-во: " + p.getQuantity() + " ед."));
                                    System.out.print("Введите ID товара для добавления: ");
                                    try {
                                        int idToAdd = Integer.parseInt(scanner.nextLine());
                                        allProducts.stream()
                                                .filter(p -> p.getId() == idToAdd)
                                                .findFirst()
                                                .ifPresentOrElse(
                                                        p -> {
                                                            userBasket.add(p);
                                                            System.out.println("✅ Товар добавлен: " + p.getProductName());
                                                        },
                                                        () -> System.out.println("❌ Товар с таким ID не найден.")
                                                );
                                    } catch (NumberFormatException e) {
                                        System.out.println("⚠ Некорректный ввод.");
                                    }
                                    break;
                                case "3":
                                    System.out.println("🧾 Товары в корзине:");
                                    // Повторяем группировку для вывода
                                    productCount.forEach((product, count) -> {
                                        System.out.println("ID: " + product.getId() + " | " + product.getProductName() + " - "
                                                + product.getPrice() + " (" + product.getManufacturer().toString() + ") Кол-во: " + count + " ед.");
                                    });
                                    System.out.print("Введите ID товара для удаления: ");
                                    try {
                                        int idToRemove = Integer.parseInt(scanner.nextLine());
                                        // Удаляем только один экземпляр товара
                                        Optional<Product> toRemove = userBasket.stream()
                                                .filter(p -> p.getId() == idToRemove)
                                                .findFirst();
                                        if (toRemove.isPresent()) {
                                            userBasket.remove(toRemove.get());
                                            System.out.println("🗑️ Удалено: " + toRemove.get().getProductName());
                                        } else {
                                            System.out.println("❌ Товар не найден в корзине.");
                                        }
                                    } catch (NumberFormatException e) {
                                        System.out.println("⚠ Некорректный ввод.");
                                    }
                                    break;
                                case "0":
                                    break;
                                default:
                                    System.out.println("⚠ Неверный ввод. Попробуйте снова.");
                            }
                            if (subChoice.equals("0") || subChoice.equals("1")) {
                                break;
                            }
                        }
                    }
                    break;



                case "5":
                    if (userBasket.isEmpty()) {
                        System.out.println("Корзина пуста. Добавьте товары.");
                    } else {
                        Order order = new Order(userBasket); // ✅

                        orders.add(order);
                        userBasket.clear();
                        System.out.println("✅ Заказ оформлен: " + order);
                    }
                    break;



                case "7":
                    if (orders.isEmpty()) {
                        System.out.println("Нет заказов для повтора.");
                    } else {
                        System.out.print("Введите номер заказа для повтора: ");
                        int id = Integer.parseInt(scanner.nextLine());
                        orders.stream().filter(o -> o.getOrderId() == id).findFirst().ifPresentOrElse(order -> {
                            userBasket.addAll(order.getProducts());
                            System.out.println("Товары из заказа добавлены в корзину.");
                        }, () -> System.out.println("Заказ не найден."));
                    }
                    break;

                case "8":
                    if (orders.isEmpty()) {
                        System.out.println("❗ Нет оформленных заказов для возврата.");
                    } else {
                        System.out.print("Введите номер заказа для возврата: ");
                        try {
                            int returnId = Integer.parseInt(scanner.nextLine());
                            Optional<Order> orderOpt = orders.stream()
                                    .filter(o -> o.getOrderId() == returnId)
                                    .findFirst();

                            if (orderOpt.isPresent()) {
                                Order order = orderOpt.get();
                                if (order.isReturned()) {
                                    System.out.println("⚠ Этот заказ уже был возвращён.");
                                } else {
                                    order.setReturned(true);
                                    System.out.println("🔁 Возврат оформлен: " + order);
                                }
                            } else {
                                System.out.println("❌ Заказ с таким номером не найден.");
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("⚠ Некорректный ввод номера заказа.");
                        }
                    }
                    break;


                case "0":
                    System.out.println("Выход из программы.");
                    return;

                default:
                    System.out.println("Нет заказов для трекинга. Выберите другое действие.");
            }
        }
    }

    private static void askToAddToBasket(Scanner scanner, List<Product> basket, List<Product> matched) {
        System.out.print("Хотите добавить товар в корзину? Введите ID товара или 0 для отмены: ");
        int id = Integer.parseInt(scanner.nextLine());
        if (id == 0) return;
        matched.stream().filter(p -> p.getId() == id).findFirst().ifPresentOrElse(
                product -> {
                    basket.add(product);
                    System.out.println("✅ Товар добавлен в корзину.");
                },
                () -> System.out.println("❌ Товар не найден по ID."));
    }
}
