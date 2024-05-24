package be.kuleuven.foodrestservice.exceptions;

public class ReservationNotFoundException extends RuntimeException {
    public ReservationNotFoundException(String mealId) {
        super("Could not find reservation for meal " + mealId);
    }
}
