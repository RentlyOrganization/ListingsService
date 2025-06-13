package rental.rentallistingservice.Exceptions;

public final class InvalidRoomsNumberException extends RuntimeException implements ValidationException{
    public InvalidRoomsNumberException(String message) {
        super(message);
    }

    @Override
    public String getErrorCode() {
        return "INVALID_ROOMS_NUMBER";
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
