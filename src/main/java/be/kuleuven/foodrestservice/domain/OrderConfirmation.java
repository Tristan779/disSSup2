package be.kuleuven.foodrestservice.domain;

import java.util.List;

public class OrderConfirmation {
    private String status;
    private String message;
    private List<String> mealNames;

    // Constructors, getters, and setters
    public OrderConfirmation(String status, String message, List<String> mealNames) {
        this.status = status;
        this.message = message;
        this.mealNames = mealNames;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
