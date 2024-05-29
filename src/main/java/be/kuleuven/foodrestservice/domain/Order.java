package be.kuleuven.foodrestservice.domain;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public class Order {
    private UUID orderId;
    private String userId;
    private Date date;
    private String street;
    private String number;
    private String city;
    private String zip;
    private String phoneNumber;
    private List<CartItem> items;
    private String status;

    // Constructors, getters, and setters
    public Order() {
    }

    public Order(String userId, String street, String number, String city, String zip, String phoneNumber, List<CartItem> items, String status) {
        this.userId = userId;
        this.street = street;
        this.number = number;
        this.city = city;
        this.zip = zip;
        this.phoneNumber = phoneNumber;
        this.items = items;
        this.status = status;
    }

    public UUID getOrderId() {
        return orderId;
    }

    public void setOrderId(UUID orderId) {
        this.orderId = orderId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public List<CartItem> getItems() {
        return items;
    }

    public void setItems(List<CartItem> items) {
        this.items = items;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
