package rental.rentallistingservice.Exceptions;

public class InvalidApartmentDataException extends RuntimeException {
    public InvalidApartmentDataException(String message) {
        super(message);
    }
}
