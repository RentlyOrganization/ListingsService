package rental.rentallistingservice.Exceptions;

public final class InvalidRentalTypeException extends RuntimeException implements ValidationException{
    public InvalidRentalTypeException(String message) {
        super(message);
    }

    @Override
    public String getErrorCode() {
        return "INVALID_RENTAL_TYPE";
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
