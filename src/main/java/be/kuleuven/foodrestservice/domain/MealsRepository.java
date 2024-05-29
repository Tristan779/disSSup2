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

    public OrderConfirmation addOrder(Order order) {

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

        List<String> mealNames = order.getItems().stream()
                .map(item -> meals.get(item.getMealId()))
                .filter(Objects::nonNull)
                .map(Meal::getName)
                .toList();

        if (mealNames.isEmpty()) {
            order.setStatus(OrderStatus.FAILED);
            orders.add(order);
            return new OrderConfirmation(order.getOrderId(), order.getStatus(), "No valid meals found for the order.", restaurantInfo.getName());
        }

        for (CartItem item : order.getItems()) {
            Meal meal = meals.get(item.getMealId());
            if (meal.getQuantity() < item.getQuantity()) {
                order.setStatus(OrderStatus.FAILED);
                orders.add(order);
                return new OrderConfirmation(order.getOrderId(), order.getStatus(), "Not enough stock for meal " + meal.getName(), restaurantInfo.getName());
            }
        }

        totalEarnings += order.getTotalPrice();
        totalOrders += order.getItems().stream()
                .mapToInt(CartItem::getQuantity)
                .sum();


        order.setStatus(OrderStatus.SUCCESS);
        orders.add(order);

        return new OrderConfirmation(order.getOrderId(), order.getStatus(), "Order placed successfully.", "Pizza Palance");
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
