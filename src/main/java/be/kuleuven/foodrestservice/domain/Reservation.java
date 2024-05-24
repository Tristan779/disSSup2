package be.kuleuven.foodrestservice.domain;

import java.time.LocalDateTime;

public class Reservation {
    private String mealId;
    private LocalDateTime reservationTime;

    // Getters and Setters

    public String getMealId() {
        return mealId;
    }

    public void setMealId(String mealId) {
        this.mealId = mealId;
    }

    public LocalDateTime getReservationTime() {
        return reservationTime;
    }

    public void setReservationTime(LocalDateTime reservationTime) {
        this.reservationTime = reservationTime;
    }
}
