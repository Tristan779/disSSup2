package be.kuleuven.foodrestservice.domain;

import java.util.List;

public class Order {
    private String address;
    private List<String> mealIds;

    // Constructors, getters, and setters
    public Order() {
    }

    public Order(String address, List<String> mealIds) {
        this.address = address;
        this.mealIds = mealIds;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<String> getMealIds() {
        return mealIds;
    }

    public void setMealIds(List<String> mealIds) {
        this.mealIds = mealIds;
    }
}
