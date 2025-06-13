package rental.rentallistingservice.Exceptions;

public final class InvalidCoordinatesException extends RuntimeException implements ValidationException{
    public InvalidCoordinatesException(String message) {
        super(message);
    }

    @Override
    public String getErrorCode() {
        return "INVALID_COORDINATES";
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
