package rental.rentallistingservice.Exceptions;

public final class InvalidApartmentIdException extends RuntimeException implements ValidationException {
    public InvalidApartmentIdException(String message) {
        super(message);
    }

    @Override
    public String getErrorCode() {
        return "INVALID_APARTMENT_ID";
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
