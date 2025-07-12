package ru.netology;

import java.util.Objects;

public class Manufacturer  {
    public String manufacturerName;
    public String country;


    public Manufacturer (String manufacturerName, String country) {
        this.manufacturerName = manufacturerName;
        this.country = country;
    }


    public String getManufacturerName() {
        return manufacturerName;
    }

    public String getCountry() {
        return country;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Manufacturer)) return false;
        Manufacturer that = (Manufacturer) o;
        return Objects.equals(manufacturerName, that.manufacturerName) &&
                Objects.equals(country, that.country);
    }

    @Override
    public int hashCode() {
        return Objects.hash(manufacturerName, country);
    }
    public String toString() {
        return manufacturerName + "(" + country + ")";
    }
}
