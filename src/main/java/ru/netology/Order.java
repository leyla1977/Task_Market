package ru.netology;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Order {
    private static int lastOrderId = 1;

    private final int orderId;
    private final List<Product> products;
    private final Date orderDate;
    private final double totalAmount;
    private String status;
    private boolean returned;



    public Order(List<Product> products) {
        this.orderId = lastOrderId++;
        this.products = new ArrayList<>(products);
        this.orderDate = new Date();
        this.totalAmount = products.stream().mapToDouble(Product::getPrice).sum();
        this.status = "В обработке";
        this.returned = false;
    }

    private double calculateTotalAmount() {
        return products.stream().mapToDouble(Product::getPrice).sum();
    }

    public int getOrderId() {
        return orderId;
    }

    public List<Product> getProducts() {
        return products;
    }

    public Date getOrderDate() {
        return orderDate;
    }


    public double getTotalAmount() {
        return totalAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isReturned() {
        return returned;
    }

    public void setReturned(boolean returned) {
        this.returned = returned;
        if (returned) {
            this.status = "Возвращён";
        }
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        LocalDateTime localDateTime = orderDate.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();

        return "📦 Заказ #" + orderId +
                " | Дата: " + localDateTime.format(formatter) +
                " | Сумма: " + totalAmount + " руб." +
                (returned ? " (Возврат оформлен)" : "");
    }

}
