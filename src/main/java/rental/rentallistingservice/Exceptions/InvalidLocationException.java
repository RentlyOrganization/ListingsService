package rental.rentallistingservice.Exceptions;

public final class InvalidLocationException extends RuntimeException implements ValidationException{
    public InvalidLocationException(String message) {
        super(message);
    }

    @Override
    public String getErrorCode() {
        return "INVALID_LOCATION";
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
