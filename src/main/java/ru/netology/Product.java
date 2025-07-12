package ru.netology;

import java.util.Objects;

public class Product {
    private final int id;
    private final  String productName;
    private final  Manufacturer manufacturer;
    private final  double price;
    private final  int quantity;
    private final  Rating rating;


    public Product (int id, String productName, Manufacturer manufacturer, double price, int quantity, Rating rating) {
        this.id = id;
        this.productName = productName;
        this.manufacturer = manufacturer;
        this.price = price;
        this.quantity = quantity;
        this.rating = rating;
    }
    public int getId() {
        return id;
    }
    public String getProductName() {
        return productName;
    }

    public String toStringForBasket(int quantityInBasket) {
        return productName + " - " + price + " (" + manufacturer + ") Кол-во в корзине: " + quantityInBasket + " ед.";
    }
    public Manufacturer getManufacturer() {
        return manufacturer;
    }
    public double getPrice () {
        return price;
    }
    public int getQuantity () {
        return quantity;
    }

    public Rating getRating() {
        return rating;
    }


    @Override
    public String toString() {
        return "ID: " + id + " | " + productName + " - " + price + " (" + manufacturer + ") Кол-во: " + quantity + " ед./кг";
    }


    public boolean isMatches(String keyWord) {
        String keywordLower = keyWord.toLowerCase();
        return (productName !=null && productName.toLowerCase().contains (keywordLower)) ||
                (manufacturer.getManufacturerName() != null && manufacturer.getManufacturerName().toLowerCase().contains (keywordLower)) ||
                (manufacturer.getCountry() !=null && manufacturer. getCountry().toLowerCase().contains (keywordLower));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true; // Сравнение ссылок
        if (o == null || getClass() != o.getClass()) return false; // Null или другой класс

        Product product = (Product) o;

        return id == product.id &&
                Double.compare(product.price, price) == 0 &&
                quantity == product.quantity &&
                productName.equals(product.productName) &&
                manufacturer.equals(product.manufacturer) &&
                rating == product.rating;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, productName, manufacturer, price, quantity, rating);
    }


}

