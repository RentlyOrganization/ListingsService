package rental.rentallistingservice.Exceptions;

public final class InvalidApartmentDataException extends RuntimeException implements ValidationException {
    public InvalidApartmentDataException(String message) {
        super(message);
    }

    @Override
    public String getErrorCode() {
        return "INVALID_APARTMENT_DATA";
    }
    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
