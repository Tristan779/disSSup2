package be.kuleuven.foodrestservice.domain;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class MealsRepository {
    private static final Map<String, Meal> meals = new HashMap<>();
    private static final RestaurantInfo restaurantInfo = new RestaurantInfo();
    private List<Order> orders = new ArrayList<>();

    private int totalOrders = 0;
    private double totalEarnings = 0.0;

    @PostConstruct
    public void initData() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            // Load data from a JSON file
            InputStream in = getClass().getResourceAsStream("/meals.json");
            JsonNode data = mapper.readTree(in);

            // Set the restaurant info
            restaurantInfo.setName(data.get("restaurantName").asText());
            restaurantInfo.setLogoUrl(data.get("restaurantLogoUrl").asText());

            // Load meals
            List<Meal> mealsFromFile = mapper.convertValue(data.get("meals"), new TypeReference<List<Meal>>(){});
            for (Meal meal : mealsFromFile) {
                // Generate a UUID for the meal
                String mealId = UUID.randomUUID().toString();
                meal.setMealId(mealId);
                meal.setRestaurantName(restaurantInfo.getName()); // Set the restaurant name for the meal
                meals.put(mealId, meal);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Optional<Meal> findMeal(String id) {
        Assert.notNull(id, "The meal id must not be null");
        Meal meal = meals.get(id);
        return Optional.ofNullable(meal);
    }

    public Collection<Meal> getAllMeal() {
        return meals.values();
    }

    public OrderConfirmation prepareOrder(Order order) {
        // Same checks as in the original addOrder method
        if (order == null ||
                order.getOrderId() == null ||
                order.getDate() == null ||
                order.getStreet() == null ||
                order.getNumber() == null ||
                order.getCity() == null ||
                order.getZip() == null ||
                order.getPhoneNumber() == null ||
                order.getItems() == null ||
                order.getTotalPrice() == null) {
            return new OrderConfirmation(null, OrderStatus.FAILED, "Invalid order data.", restaurantInfo.getName());
        }

        for (CartItem item : order.getItems()) {
            Meal meal = meals.get(item.getMealId());
            if (meal == null) {
                return new OrderConfirmation(order.getOrderId(), OrderStatus.FAILED, "Invalid meal ID found in the order: " + item.getMealId(), restaurantInfo.getName());
            }
        }

        for (CartItem item : order.getItems()) {
            Meal meal = meals.get(item.getMealId());
            if (meal.getQuantity() < item.getQuantity()) {
                return new OrderConfirmation(order.getOrderId(), OrderStatus.FAILED, "Not enough stock for meal " + meal.getName(), restaurantInfo.getName());
            }
        }

        // If all checks pass, return a positive response without modifying the order or the meals
        return new OrderConfirmation(order.getOrderId(), OrderStatus.PENDING, "Order can be placed.", restaurantInfo.getName());
    }


    public OrderConfirmation commitOrder(Order order) {
        // Assume that the order has been prepared and can be placed

        // Update the quantity of the meals
        for (CartItem item : order.getItems()) {
            Meal meal = meals.get(item.getMealId());
            meal.setQuantity(meal.getQuantity() - item.getQuantity());
        }

        totalEarnings += order.getTotalPrice();
        totalOrders += order.getItems().stream()
                .mapToInt(CartItem::getQuantity)
                .sum();

        order.setStatus(OrderStatus.SUCCESS);
        orders.add(order);

        return new OrderConfirmation(order.getOrderId(), order.getStatus(), "Order placed successfully.", restaurantInfo.getName());
    }

    public List<Order> getAllOrders() {
        return orders;
    }

    public RestaurantInfo getRestaurantInfo() {
        return restaurantInfo;
    }

    public int getTotalOrders() {
        return totalOrders;
    }

    public double getTotalEarnings() {
        return totalEarnings;
    }
}
