package rental.rentallistingservice.Exceptions;

public final class InvalidParameterException extends RuntimeException implements ValidationException{
    public InvalidParameterException(String message) {
        super(message);
    }

    @Override
    public String getErrorCode() {
        return "INVALID_PARAMETER";
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
