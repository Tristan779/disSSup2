package be.kuleuven.foodrestservice.domain;

import java.util.List;

public class OrderConfirmation {
    private String message;
    private List<String> mealNames;

    // Constructors, getters, and setters
    public OrderConfirmation(String message, List<String> mealNames) {
        this.message = message;
        this.mealNames = mealNames;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<String> getMealNames() {
        return mealNames;
    }

    public void setMealNames(List<String> mealNames) {
        this.mealNames = mealNames;
    }
}
