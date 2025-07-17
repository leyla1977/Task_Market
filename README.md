# Task_Market

Избегание магических чисел
Все "магические числа" (например, коды меню, фиксированные значения и т.п.) вынесены в константы или параметры, например, в **классе Rating -  [ссылка](https://github.com/leyla1977/Task_Market/blob/main/src/main/java/ru/netology/Rating.java):**


    private static final double PRODUCT_WEIGHT = 0.5;
    private static final double MANUFACTURER_WEIGHT = 0.2;
    private static final double DELIVERY_WEIGHT = 0.15;
    private static final double PACKAGE_WEIGHT = 0.15;
    


**Принципы SOLID**

**S — Single Responsibility Principle (Принцип единственной ответственности)
Каждый класс отвечает за одну конкретную задачу.**

Пример в коде:

Basket.java — управление корзиной -  [ссылка](https://github.com/leyla1977/Task_Market/blob/main/src/main/java/ru/netology/Basket.java):
- добавление товаров
     public void addProduct(Product product) {
        items.put(product, items.getOrDefault(product, 0) + 1);
    }
- удаление товаров
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
- общая сумма товаров
-    public double getTotalAmount() {
        double total = 0;
        for (Map.Entry<Product, Integer> entry : items.entrySet()) {
            total += entry.getKey().getPrice() * entry.getValue();
        }
        return total;
    }

Order.java — хранение информации о заказе и его статусе - [ссылка](https://github.com/leyla1977/Task_Market/blob/main/src/main/java/ru/netology/Order.java).


**O — Open/Closed Principle (Принцип открытости/закрытости)
Классы и модули открыты для расширения, но закрыты для модификации.**

Это реализвано в классе ProductSearchService  -  [ссылка](https://github.com/leyla1977/Task_Market/blob/main/src/main/java/ru/netology/ProductSearchService.java). Здесь можно видеть, что метод search использует метод p.isMatches(keyword) внутри фильтра. Это означает, что поведение фильтрации зависит от реализации метода isMatches в классе Product.
Если в будущем потребуется изменить или расширить логику поиска, можно просто изменить поведение в переопределенном методе isMatches() в наследниках Product, не меняя код самого класса ProductSearchService.Таким образом, класс открыт для расширения (можно добавлять новые типы продуктов с новой логикой поиска), но закрыт для модификации (не нужно менять ProductSearchService).



**L — Liskov Substitution Principle (Принцип подстановки Барбары Лисков)
Проектируем архитектуру с возможностью расширения базовых классов наследниками без изменения потребителей.**
Не реализован

**I — Interface Segregation Principle (Принцип разделения интерфейсов)
Каждый класс зависит только от нужных ему методов.**

Класс BasketBuilder [ссылка](https://github.com/leyla1977/Task_Market/blob/main/src/main/java/ru/netology/BasketBuilder.java)  работает с любым объектом, реализующим PurchaseStatsProvider [ссылка](https://github.com/leyla1977/Task_Market/blob/main/src/main/java/ru/netology/PurchaseStatsProvider.java), а не только с PurchaseHistory [ссылка](https://github.com/leyla1977/Task_Market/blob/main/src/main/java/ru/netology/PurchaseHistory.java). Это и есть гибкость, достигаемая за счёт разделения интерфейсов.
Поясню. По логике ISP классы не должны зависеть от методов, которые они не используют. Интерфейсы должны быть маленькими и специфичными.
Что у меня:
1. Есть интерфейс :
public interface PurchaseStatsProvider {
    Map<Product, Integer> getPurchaseStats();
}
2. Я его применяю в PurchaseHistory:
public class PurchaseHistory implements PurchaseStatsProvider {
    ...
}
3.А также в BasketBuider:
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
Таким образом ISP реализован, хоть и по минимуму:
PurchaseStatsProvider — это маленький, узкоспециализированный интерфейс, содержащий только один метод.
Класс BasketBuilder использует только то, что ему нужно: метод getPurchaseStats(), а не весь PurchaseHistory.
Таким образом:
1. Интерфейс PurchaseStatsProvider маленький и специфичный.
2. BasketBuilder использует только нужные методы интерфейса, и интерфейс не "заставляет"  реализовать "лишние" методы.

**D — Dependency Inversion Principle (Принцип инверсии зависимостей)**
Согласно DIP pависимости должны быть от абстракций, а не от конкретных классов.
Модули верхнего уровня не должны зависеть от модулей нижнего уровня напрямую.
В этой программе модуль BasketBuilder  -  [ссылка](https://github.com/leyla1977/Task_Market/blob/main/src/main/java/ru/netology/BasketBuilder.java)  не зависит от конкретной реализации, он зависит от абстракции PurchaseStatsProvider, что делает архитектуру гибкой и расширяемой. Таким образом DIP реализован:
1. Создан интерфейс PurchaseStatsProvider - [ссылка](https://github.com/leyla1977/Task_Market/blob/main/src/main/java/ru/netology/PurchaseStatsProvider.java), который предоставляет статистику покупок:


public interface PurchaseStatsProvider {
    Map<Product, Integer> getPurchaseStats();
}
2.Класс PurchaseHistory - [ссылка](https://github.com/leyla1977/Task_Market/blob/main/src/main/java/ru/netology/PurchaseHistory.java) реализует этот интерфейс:

public class PurchaseHistory implements PurchaseStatsProvider {
    ...
}
3. Класс BasketBuilder - [ссылка](https://github.com/leyla1977/Task_Market/blob/main/src/main/java/ru/netology/BasketBuilder.java)  теперь зависит не от PurchaseHistory, а от интерфейса:

public class BasketBuilder {
    private final PurchaseStatsProvider statsProvider;

    public BasketBuilder(PurchaseStatsProvider statsProvider) {
        this.statsProvider = statsProvider;
    }
}
4. В main() - [ссылка](https://github.com/leyla1977/Task_Market/blob/main/src/main/java/ru/netology/Main.java) (строки 38-41) внедрена  зависимость:

PurchaseStatsProvider purchaseHistory = new PurchaseHistory();
BasketBuilder basketBuilder = new BasketBuilder(purchaseHistory);

**Принцип DRY (Don't Repeat Yourself)**
**1. Метод askToAddToBasket в BasketHelper -  [ссылка](https://github.com/leyla1977/Task_Market/blob/main/src/main/java/ru/netology/BasketHelper.java):**

public static void askToAddToBasket(Scanner scanner, Basket userBasket, List<Product> products)
Код добавления товара в корзину  — он выделен в отдельный метод, который можно переиспользовать после любого поиска (по ключевому слову или по производителю):
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
                      **  askToAddToBasket(scanner, userBasket, byManufacturer);**
                    }
                    break;
                }. 
                Это чистое применение DRY. Так мы  не дублируем код добавления в корзину, а просто передаем:

-scanner — для работы с вводом;

-userBasket — в которую добавлять;

-byManufacturer — список, из которого выбирать.


**2. Метод getAllItemsAsList в Basket -  [ссылка](https://github.com/leyla1977/Task_Market/blob/main/src/main/java/ru/netology/Basket.java):**
Возвращает список всех товаров в корзине с учетом количества каждого товара, то есть с "разворачиванием" дубликатов.
    public List<Product> getAllItemsAsList() {
        List<Product> allItems = new ArrayList<>();
        for (Map.Entry<Product, Integer> entry : items.entrySet()) {
            Product product = entry.getKey();
            int quantity = entry.getValue();
            for (int i = 0; i < quantity; i++) {
                allItems.add(product);
            }
        }
        return allItems;
    }
Есть несколько мест, где нужно получить все товары с учётом их количества — например:

1. // Группируем товары в корзине по продукту и считаем количество каждого
                        List<Product> allItemsInBasket = userBasket.getAllItemsAsList();
    
2.       case "1":
                                    Order newOrder = new Order(userBasket.getAllItemsAsList());
                                    orders.add(newOrder);
                                    System.out.println("📦 Заказ оформлен! Номер заказа: " + newOrder.getOrderId());
3.       case "3":
                                    System.out.println("\uD83D\uDCDD Товары в корзине:");
                                    List<Product> basketItems = userBasket.getAllItemsAsList();
4.       case "5":
                    if (userBasket.getProducts().isEmpty()) {
                        System.out.println("Корзина пуста. Добавьте товары.");
                    } else {
                        Order order = new Order(userBasket.getAllItemsAsList());
5.     // Добавляем товары из корзины в историю покупок
                        for (Product p : userBasket.getAllItemsAsList()) {
                            purchaseHistory.addPurchase(p);
                        }


