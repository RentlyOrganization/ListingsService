package rental.rentallistingservice.Exceptions;

public class InvalidApartmentIdException extends RuntimeException {
    public InvalidApartmentIdException(String message) {
        super(message);
    }
}
