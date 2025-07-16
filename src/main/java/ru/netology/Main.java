package ru.netology;

import java.util.*;
import java.util.stream.Collectors;
import java.util.List;
import java.util.Map;

import static ru.netology.BasketHelper.askToAddToBasket;

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
        PurchaseHistory purchaseHistory = new PurchaseHistory();
        Basket userBasket = new Basket();
        List<Order> orders = new ArrayList<>();
        BasketBuilder basketBuilder = new BasketBuilder(purchaseHistory);




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
            System.out.println("9. Рекомендуем на основе ваших покупок");
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
                                                    userBasket.addProduct(product);  // Так нужно
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



                case "3": {
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
                }


                case "4":
                    if (userBasket.isEmpty()) {
                        System.out.println("🛒 Корзина пуста.");
                    } else {
                        System.out.println("🛒 Товары в корзине:");

                        // Группируем товары в корзине по продукту и считаем количество каждого
                        List<Product> allItemsInBasket = userBasket.getAllItemsAsList();

                        Map<Product, Long> productCount = allItemsInBasket.stream()
                                .collect(Collectors.groupingBy(p -> p, Collectors.counting()));

                        productCount.forEach((product, count) -> {
                            System.out.println("ID: " + product.getId() + " | " + product.getProductName() + " - "
                                    + product.getPrice() + " (" + product.getManufacturer().toString() + ") Кол-во: " + count + " ед.");
                        });


                        double total = allItemsInBasket.stream().mapToDouble(Product::getPrice).sum();
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
                                    Order newOrder = new Order(userBasket.getAllItemsAsList());
                                    orders.add(newOrder);
                                    System.out.println("📦 Заказ оформлен! Номер заказа: " + newOrder.getOrderId());

                                    //  Добавим все товары из корзины в историю покупок
                                    for (Product p : userBasket.getProducts()) {
                                        int qty = userBasket.getQuantity(p);
                                        for (int i = 0; i < qty; i++) {
                                            purchaseHistory.addPurchase(p);
                                        }
                                    }

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
                                                            userBasket.addProduct(p);

                                                            System.out.println("✅ Товар добавлен: " + p.getProductName());
                                                        },
                                                        () -> System.out.println("❌ Товар с таким ID не найден.")
                                                );
                                    } catch (NumberFormatException e) {
                                        System.out.println("⚠ Некорректный ввод.");
                                    }
                                    break;
                                case "3":
                                    System.out.println("\uD83D\uDCDD Товары в корзине:");
                                    List<Product> basketItems = userBasket.getAllItemsAsList();
                                    Map<Product, Long> itemsCount = basketItems.stream()
                                            .collect(Collectors.groupingBy(p -> p, Collectors.counting()));


                                    productCount.forEach((product, count) -> {
                                        System.out.println("ID: " + product.getId() + " | " + product.getProductName() + " - "
                                                + product.getPrice() + " (" + product.getManufacturer().toString() + ") Кол-во: " + count + " ед.");
                                    });
                                    System.out.print("Введите ID товара для удаления: ");
                                    try {
                                        int idToRemove = Integer.parseInt(scanner.nextLine());
                                        Optional<Product> productToRemove = productCount.keySet().stream()
                                                .filter(p -> p.getId() == idToRemove)
                                                .findFirst();

                                        productToRemove.ifPresentOrElse(
                                                product -> {
                                                    userBasket.removeProduct(product);
                                                    System.out.println("❎ Удалён один экземпляр товара: " + product.getProductName());
                                                },
                                                () -> System.out.println("❌ Товар с таким ID не найден в корзине.")
                                        );
                                    } catch (NumberFormatException e) {
                                        System.out.println("⚠ Некорректный ввод.");
                                    }
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
                    if (userBasket.getProducts().isEmpty()) {
                        System.out.println("Корзина пуста. Добавьте товары.");
                    } else {
                        Order order = new Order(userBasket.getAllItemsAsList()); // ✅

                        orders.add(order);

                        // Добавляем товары из корзины в историю покупок
                        for (Product p : userBasket.getAllItemsAsList()) {
                            purchaseHistory.addPurchase(p);
                        }
                        
                        userBasket.clear();
                        System.out.println("✅ Заказ оформлен: " + order);
                    }
                    break;

                case "6":
                    if (orders.isEmpty()) {
                        System.out.println("Нет заказов для трекинга. Выберите другое действие.");
                    } else {
                        System.out.print("Введите номер заказа для трекинга: ");
                        try {
                            int orderId = Integer.parseInt(scanner.nextLine());

                            orders.stream()
                                    .filter(order -> order.getOrderId() == orderId)
                                    .findFirst()
                                    .ifPresentOrElse(order -> {
                                        // Здесь можно вывести информацию о заказе, например:
                                        System.out.println("Заказ найден:");
                                        System.out.println("Номер заказа: " + order.getOrderId());
                                        System.out.println("Дата заказа: " + order.getOrderDate());
                                        System.out.println("Сумма: " + order.getTotalAmount() + " руб.");
                                        // Можно вывести детали заказа:
                                        order.getProducts().forEach(product ->
                                                System.out.println("- " + product.getProductName() + " | Цена: " + product.getPrice())
                                        );
                                    }, () -> System.out.println("Заказ с номером " + orderId + " не найден."));
                        } catch (NumberFormatException e) {
                            System.out.println("Некорректный ввод номера заказа.");
                        }
                    }
                    break;


                case "7":
                    if (orders.isEmpty()) {
                        System.out.println("Нет заказов для повтора.");
                    } else {
                        System.out.print("Введите номер заказа для повтора: ");
                        try {
                            int id = Integer.parseInt(scanner.nextLine());

                            orders.stream()
                                    .filter(o -> o.getOrderId() == id)
                                    .findFirst()
                                    .ifPresentOrElse(order -> {
                                        for (Product p : order.getProducts()) {
                                            userBasket.addProduct(p); // добавляем каждый товар из заказа в корзину
                                        }
                                        System.out.println("Товары из заказа добавлены в корзину.");
                                    }, () -> System.out.println("❌ Заказ не найден."));

                        } catch (NumberFormatException e) {
                            System.out.println("⚠ Некорректный номер заказа.");
                        }
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
                case "9":
                    List<Product> recommended = basketBuilder.buildBasket(3);
                    if (recommended.isEmpty()) {
                        System.out.println("Недостаточно данных для рекомендаций.");
                    } else {
                        System.out.println("🧠 Рекомендуем на основе ваших покупок:");
                        recommended.forEach(p -> System.out.println(p));
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


}
