package be.kuleuven.foodrestservice.domain;

import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import be.kuleuven.foodrestservice.exceptions.MealNotFoundException;

@Component
public class MealsRepository {
    private static final Map<String, Meal> meals = new HashMap<>();
    private static final RestaurantInfo restaurantInfo = new RestaurantInfo();
    private static final Map<String, Reservation> reservations = new HashMap<>();
    private List<Order> orders = new ArrayList<>();

    private int totalOrders = 0;
    private double totalEarnings = 0.0;

    @PostConstruct
    public void initData() {
        restaurantInfo.setName("Pattty Paradise");
        restaurantInfo.setLogoUrl("https://supplierphotos.blob.core.windows.net/images/pattty_paradise_logo.png");

        Meal a = new Meal();
        a.setId("bf94f1b5-4b27-41e8-9a84-3dbdf5b56e1e");
        a.setName("Classic Beef Burger");
        a.setDescription("Juicy beef patty with lettuce, tomato, and cheese");
        a.setMealType(MealType.MEAT);
        a.setKcal(800);
        a.setPrice(8.00);
        a.setMealPhoto("https://supplierphotos.blob.core.windows.net/images/burger_beef.png");
        a.setQuantity(10);
        meals.put(a.getId(), a);

        Meal b = new Meal();
        b.setId("14a98a12-6f0a-4c2f-9c6a-1d3ff2a2bb37");
        b.setName("Vegan Black Bean Burger");
        b.setDescription("Delicious black bean patty with avocado and vegan cheese");
        b.setMealType(MealType.VEGAN);
        b.setKcal(600);
        b.setPrice(7.50);
        b.setMealPhoto("https://supplierphotos.blob.core.windows.net/images/burger_beans.png");
        b.setQuantity(10);
        meals.put(b.getId(), b);

        Meal c = new Meal();
        c.setId("56f61077-c33e-4318-9bdb-7b8724a1dd1c");
        c.setName("Fish Fillet Burger");
        c.setDescription("Crispy fish fillet with tartar sauce and lettuce");
        c.setMealType(MealType.FISH);
        c.setKcal(750);
        c.setPrice(9.00);
        c.setMealPhoto("https://supplierphotos.blob.core.windows.net/images/burger_fish.png");
        c.setQuantity(10);
        meals.put(c.getId(), c);

        Meal d = new Meal();
        d.setId("3d1f3c17-3c7b-4f1b-a1b1-5ff635ccfef1");
        d.setName("Veggie Delight Burger");
        d.setDescription("Grilled veggie patty with lettuce, tomato, and avocado");
        d.setMealType(MealType.VEGGIE);
        d.setKcal(650);
        d.setPrice(7.00);
        d.setMealPhoto("https://supplierphotos.blob.core.windows.net/images/burger_veggie.png");
        d.setQuantity(10);
        meals.put(d.getId(), d);

        Meal e = new Meal();
        e.setId("c4e8e5c8-87c4-4ed6-b5c5-59dd1c0e2f9e");
        e.setName("Double Bacon Cheeseburger");
        e.setDescription("Double beef patty with bacon, cheddar cheese, and BBQ sauce");
        e.setMealType(MealType.MEAT);
        e.setKcal(1200);
        e.setPrice(10.00);
        e.setMealPhoto("https://supplierphotos.blob.core.windows.net/images/burger_bacon.png");
        e.setQuantity(10);
        meals.put(e.getId(), e);
    }

    public Optional<Meal> findMeal(String id) {
        Assert.notNull(id, "The meal id must not be null");
        Meal meal = meals.get(id);
        return Optional.ofNullable(meal);
    }

    public Collection<Meal> getAllMeal() {
        return meals.values();
    }

    public Meal getCheapestMeal() {
        return meals.values().stream()
                .min(Comparator.comparing(Meal::getPrice))
                .orElseThrow(() -> new MealNotFoundException("No meals found"));
    }

    public Meal getLargestMeal() {
        return meals.values().stream()
                .max(Comparator.comparing(Meal::getKcal))
                .orElseThrow(() -> new MealNotFoundException("No meals found"));
    }

    public void addMeal(Meal meal) {
        meals.put(meal.getId(), meal);
    }

    public Meal updateMeal(String id, Meal updatedMeal) {
        if (!meals.containsKey(id)) {
            return null;
        }
        updatedMeal.setId(id);
        meals.put(id, updatedMeal);
        return updatedMeal;
    }

    public boolean deleteMeal(String id) {
        if (!meals.containsKey(id)) {
            return false;
        }
        meals.remove(id);
        return true;
    }

    public OrderConfirmation addOrder(Order order) {
        List<String> mealNames = order.getMealIds().stream()
                .map(meals::get)
                .filter(Objects::nonNull)
                .map(Meal::getName)
                .collect(Collectors.toList());

        if (mealNames.isEmpty()) {
            throw new MealNotFoundException("No valid meals found for the order.");
        }

        // Update meal quantities and restaurant earnings
        for (String mealId : order.getMealIds()) {
            Meal meal = meals.get(mealId);
            if (meal.getQuantity() <= 0) {
                throw new MealNotFoundException("Meal " + meal.getName() + " is out of stock.");
            }
            meal.setQuantity(meal.getQuantity() - 1);
            totalEarnings += meal.getPrice();
            totalOrders++;
        }

        // Add order to the list of orders
        orders.add(order);

        return new OrderConfirmation("Order placed successfully.", mealNames);
    }

    public List<Order> getAllOrders() {
        return orders;
    }

    public RestaurantInfo getRestaurantInfo() {
        return restaurantInfo;
    }

    public void addReservation(String mealId, LocalDateTime reservationTime) {
        Reservation reservation = new Reservation();
        reservation.setMealId(mealId);
        reservation.setReservationTime(reservationTime);
        reservations.put(mealId, reservation);
    }

    public void cancelReservation(String mealId) {
        reservations.remove(mealId);
    }

    public Reservation getReservation(String mealId) {
        return reservations.get(mealId);
    }

    public int getTotalOrders() {
        return totalOrders;
    }

    public double getTotalEarnings() {
        return totalEarnings;
    }
}