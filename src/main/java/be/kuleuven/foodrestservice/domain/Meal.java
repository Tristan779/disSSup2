package be.kuleuven.foodrestservice.domain;

import java.util.Objects;

public class Meal {
    protected String mealId;
    protected String restaurantName;
    protected String name;
    protected Double price;
    protected String imagelUrl;

    // Getters and Setters

    public String getMealId() {
        return mealId;
    }

    public void setId(String id) {
        this.mealId = id;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getImageUrl() {
        return imagelUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imagelUrl = imageUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Meal meal = (Meal) o;
        return Objects.equals(mealId, meal.mealId) &&
                Objects.equals(restaurantName, meal.restaurantName) &&
                Objects.equals(name, meal.name) &&
                Objects.equals(price, meal.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mealId, restaurantName, name, price);
    }
}
