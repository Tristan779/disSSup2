package be.kuleuven.foodrestservice.domain;

public class CartItem {
    private String mealId;
    private int quantity;

    public CartItem() {
    }

    public CartItem(String mealId, int quantity) {
        this.mealId = mealId;
        this.quantity = quantity;
    }

    public String getMealId() {
        return mealId;
    }

    public void setMealId(String mealId) {
        this.mealId = mealId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
