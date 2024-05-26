package be.kuleuven.foodrestservice.controllers;

import be.kuleuven.foodrestservice.domain.*;
import be.kuleuven.foodrestservice.exceptions.MealNotFoundException;
import be.kuleuven.foodrestservice.exceptions.ReservationNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
public class MealsRestController {

    private final MealsRepository mealsRepository;

    @Autowired
    MealsRestController(MealsRepository mealsRepository) {
        this.mealsRepository = mealsRepository;
    }

    @GetMapping("/rest/meals/{mealId}")
    EntityModel<Meal> getMealById(@PathVariable String mealId) {
        Meal meal = mealsRepository.findMeal(mealId).orElseThrow(() -> new MealNotFoundException(mealId));
        return mealToEntityModel(mealId, meal);
    }

    @GetMapping("/rest/meals")
    CollectionModel<EntityModel<Meal>> getMeals() {
        Collection<Meal> meals = mealsRepository.getAllMeal();

        List<EntityModel<Meal>> mealEntityModels = new ArrayList<>();
        for (Meal m : meals) {
            EntityModel<Meal> em = mealToEntityModel(m.getMealId(), m);
            mealEntityModels.add(em);
        }
        return CollectionModel.of(mealEntityModels,
                linkTo(methodOn(MealsRestController.class).getMeals()).withSelfRel());
    }

    private EntityModel<Meal> mealToEntityModel(String mealId, Meal meal) {
        return EntityModel.of(meal,
                linkTo(methodOn(MealsRestController.class).getMealById(mealId)).withSelfRel(),
                linkTo(methodOn(MealsRestController.class).getMeals()).withRel("rest/meals"));
    }

    @PostMapping("/rest/reservations")
    public ResponseEntity<?> makeReservation(@RequestBody Reservation reservation) {
        mealsRepository.addReservation(reservation.getMealId(), reservation.getReservationTime());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/rest/reservations/{mealId}")
    public ResponseEntity<?> cancelReservation(@PathVariable String mealId) {
        mealsRepository.cancelReservation(mealId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/rest/reservations/{mealId}")
    public EntityModel<Reservation> getReservation(@PathVariable String mealId) {
        Reservation reservation = mealsRepository.getReservation(mealId);
        if (reservation == null) {
            throw new ReservationNotFoundException(mealId);
        }
        return EntityModel.of(reservation,
                linkTo(methodOn(MealsRestController.class).getReservation(mealId)).withSelfRel());
    }

    @GetMapping("/rest/restaurant")
    public EntityModel<RestaurantInfo> getRestaurantInfo() {
        RestaurantInfo restaurantInfo = mealsRepository.getRestaurantInfo();
        return EntityModel.of(restaurantInfo,
                linkTo(methodOn(MealsRestController.class).getRestaurantInfo()).withSelfRel());
    }

    @GetMapping("/rest/restaurant/orders")
    public ResponseEntity<Map<String, Object>> getRestaurantOrdersAndEarnings() {
        Map<String, Object> response = new HashMap<>();
        response.put("totalOrders", mealsRepository.getTotalOrders());
        response.put("totalEarnings", mealsRepository.getTotalEarnings());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/rest/orders")
    public EntityModel<OrderConfirmation> placeOrder(@RequestBody Order order) {
        OrderConfirmation confirmation = mealsRepository.addOrder(order);
        return EntityModel.of(confirmation,
                linkTo(methodOn(MealsRestController.class).placeOrder(order)).withSelfRel(),
                linkTo(methodOn(MealsRestController.class).getMeals()).withRel("meals"));
    }

    @GetMapping("/rest/orders")
    public ResponseEntity<CollectionModel<EntityModel<Order>>> getAllOrders() {
        List<EntityModel<Order>> orderEntityModels = mealsRepository.getAllOrders().stream()
                .map(order -> EntityModel.of(order,
                        linkTo(methodOn(MealsRestController.class).getAllOrders()).withSelfRel()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(CollectionModel.of(orderEntityModels,
                linkTo(methodOn(MealsRestController.class).getAllOrders()).withSelfRel()));
    }
}
