package ru.netology;

import java.util.Objects;

public class Rating {
    private final int productQual;
    private final int manufacturerQual;
    private final int deliveryQual;
    private final int packageQual;

    private static final double PRODUCT_WEIGHT = 0.5;
    private static final double MANUFACTURER_WEIGHT = 0.2;
    private static final double DELIVERY_WEIGHT = 0.15;
    private static final double PACKAGE_WEIGHT = 0.15;

    public Rating (int productQual, int manufacturerQual, int deliveryQual, int packageQual) {
        this.productQual = productQual;
        this.manufacturerQual = manufacturerQual;
        this.deliveryQual = deliveryQual;
        this.packageQual = packageQual;

    }

    public int getProductQual() {
        return productQual;
    }

    public int getManufactorerQual() {
        return manufacturerQual;
    }

    public int getDeliveryQual() {
        return deliveryQual;
    }

    public int getPackageQual() {
        return packageQual;
    }

    public double calculateAverage() {
        return (productQual + manufacturerQual + deliveryQual + packageQual) / 4.0;
    }

    public double getOverallRating() {
        return productQual * PRODUCT_WEIGHT +
                manufacturerQual * MANUFACTURER_WEIGHT +
                deliveryQual * DELIVERY_WEIGHT +
                packageQual * PACKAGE_WEIGHT;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Rating)) return false;
        Rating rating = (Rating) o;
        return productQual == rating.productQual &&
                manufacturerQual == rating.manufacturerQual &&
                deliveryQual == rating.deliveryQual &&
                packageQual == rating.packageQual;
    }



    @Override
    public int hashCode() {
        return Objects.hash(productQual, manufacturerQual, deliveryQual, packageQual);
    }



    @Override
    public String toString() {
        return String.format("Рейтинг: %.2f (товар: %d, производитель: %d, доставка: %d, упаковка: %d)",
                getOverallRating(), productQual, manufacturerQual, deliveryQual, packageQual);
    }


}
