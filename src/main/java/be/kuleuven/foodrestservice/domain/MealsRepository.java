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
        restaurantInfo.setName("Pizza Palance");
        restaurantInfo.setLogoUrl("https://supplierphotos.blob.core.windows.net/images/pizza_palance_logo.png");

        Meal a = new Meal();
        a.setId("bf94f1b5-4b27-41e8-9a84-3dbdf5b56e1e");
        a.setRestaurantName("Pizza Palance");
        a.setName("Margherita Pizza");
        a.setPrice(8.00);
        a.setImageUrl("https://supplierphotos.blob.core.windows.net/images/pizza_margherita.png");
        meals.put(a.getMealId(), a);

        Meal b = new Meal();
        b.setId("14a98a12-6f0a-4c2f-9c6a-1d3ff2a2bb37");
        b.setRestaurantName("Pizza Palance");
        b.setName("Pepperoni Pizza");
        b.setPrice(7.50);
        b.setImageUrl("https://supplierphotos.blob.core.windows.net/images/pizza_pepperoni.png");
        meals.put(b.getMealId(), b);

        Meal c = new Meal();
        c.setId("56f61077-c33e-4318-9bdb-7b8724a1dd1c");
        c.setRestaurantName("Pizza Palance");
        c.setName("Hawaiian Pizza");
        c.setPrice(9.00);
        c.setImageUrl("https://supplierphotos.blob.core.windows.net/images/pizza_hawaiian.png");
        meals.put(c.getMealId(), c);

        Meal d = new Meal();
        d.setId("3d1f3c17-3c7b-4f1b-a1b1-5ff635ccfef1");
        d.setRestaurantName("Pizza Palance");
        d.setName("Veggie Pizza");
        d.setPrice(7.00);
        d.setImageUrl("https://supplierphotos.blob.core.windows.net/images/pizza_veggie.png");
        meals.put(d.getMealId(), d);

        Meal e = new Meal();
        e.setId(UUID.randomUUID().toString());
        e.setRestaurantName("Pizza Palance");
        e.setName("BBQ Chicken Pizza");
        e.setPrice(9.50);
        e.setImageUrl("https://supplierphotos.blob.core.windows.net/images/pizza_bbq_chicken.png");
        meals.put(e.getMealId(), e);

        Meal f = new Meal();
        f.setId(UUID.randomUUID().toString());
        f.setRestaurantName("Pizza Palance");
        f.setName("Buffalo Pizza");
        f.setPrice(8.50);
        f.setImageUrl("https://supplierphotos.blob.core.windows.net/images/pizza_buffalo.png");
        meals.put(f.getMealId(), f);

        Meal g = new Meal();
        g.setId(UUID.randomUUID().toString());
        g.setRestaurantName("Pizza Palance");
        g.setName("Cheese Pizza");
        g.setPrice(7.00);
        g.setImageUrl("https://supplierphotos.blob.core.windows.net/images/pizza_cheese.png");
        meals.put(g.getMealId(), g);

        Meal h = new Meal();
        h.setId(UUID.randomUUID().toString());
        h.setRestaurantName("Pizza Palance");
        h.setName("Mushroom Pizza");
        h.setPrice(8.00);
        h.setImageUrl("https://supplierphotos.blob.core.windows.net/images/pizza_mushroom.png");
        meals.put(h.getMealId(), h);
    }


    public Optional<Meal> findMeal(String id) {
        Assert.notNull(id, "The meal id must not be null");
        Meal meal = meals.get(id);
        return Optional.ofNullable(meal);
    }

    public Collection<Meal> getAllMeal() {
        return meals.values();
    }


    public void addMeal(Meal meal) {
        meals.put(meal.getMealId(), meal);
    }

    public Meal updateMeal(String mealId, Meal updatedMeal) {
        if (!meals.containsKey(mealId)) {
            return null;
        }
        updatedMeal.setId(mealId);
        meals.put(mealId, updatedMeal);
        return updatedMeal;
    }

    public boolean deleteMeal(String mealId) {
        if (!meals.containsKey(mealId)) {
            return false;
        }
        meals.remove(mealId);
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