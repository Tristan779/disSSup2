package be.kuleuven.foodrestservice.controllers;

import be.kuleuven.foodrestservice.domain.Meal;
import be.kuleuven.foodrestservice.domain.MealsRepository;
import be.kuleuven.foodrestservice.exceptions.MealNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import be.kuleuven.foodrestservice.domain.Order;
import be.kuleuven.foodrestservice.domain.OrderConfirmation;


import java.util.Collection;
import java.util.Optional;

@RestController
public class MealsRestRpcStyleController {

    private final MealsRepository mealsRepository;

    @Autowired
    MealsRestRpcStyleController(MealsRepository mealsRepository) {
        this.mealsRepository = mealsRepository;
    }

    @GetMapping("/restrpc/meals/{id}")
    Meal getMealById(@PathVariable String id) {
        Optional<Meal> meal = mealsRepository.findMeal(id);

        return meal.orElseThrow(() -> new MealNotFoundException(id));
    }

    @GetMapping("/restrpc/meals")
    Collection<Meal> getMeals() {
        return mealsRepository.getAllMeal();
    }

    @GetMapping("/restrpc/meals/cheapest")
    public Meal getCheapestMeal() {
        return mealsRepository.getCheapestMeal();
    }

    @GetMapping("/restrpc/meals/largest")
    public Meal getLargestMeal() {
        return mealsRepository.getLargestMeal();
    }

    @PostMapping("/restrpc/meals")
    public ResponseEntity<Meal> addMeal(@RequestBody Meal meal) {
        mealsRepository.addMeal(meal);
        return new ResponseEntity<>(meal, HttpStatus.CREATED);
    }

    @PutMapping("/restrpc/meals/{id}")
    public ResponseEntity<Meal> updateMeal(@PathVariable String id, @RequestBody Meal updatedMeal) {
        Meal meal = mealsRepository.updateMeal(id, updatedMeal);
        if (meal == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(meal, HttpStatus.OK);
    }

    @DeleteMapping("/restrpc/meals/{id}")
    public ResponseEntity<Void> deleteMeal(@PathVariable String id) {
        boolean deleted = mealsRepository.deleteMeal(id);
        if (!deleted) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @PostMapping("/restrpc/orders")
    public ResponseEntity<OrderConfirmation> addOrder(@RequestBody Order order) {
        OrderConfirmation confirmation = mealsRepository.addOrder(order);
        return new ResponseEntity<>(confirmation, HttpStatus.CREATED);
    }
}
