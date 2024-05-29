package be.kuleuven.foodrestservice.domain;

import java.util.UUID;

public class OrderConfirmation {
    private UUID orderId;
    private String status;
    private String message;
    private String restaurantName;

    public OrderConfirmation(UUID orderId, String status, String message, String restaurantName) {
        this.orderId = orderId;
        this.status = status;
        this.message = message;
        this.restaurantName = restaurantName;
    }

    public UUID getOrderId() {
        return orderId;
    }

    public void setOrderId(UUID orderId) {
        this.orderId = orderId;
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

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

}
