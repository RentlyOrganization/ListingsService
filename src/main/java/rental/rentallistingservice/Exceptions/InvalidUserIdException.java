package rental.rentallistingservice.Exceptions;

public final class InvalidUserIdException extends RuntimeException implements ValidationException{
    public InvalidUserIdException(String message) {
        super(message);
    }

    @Override
    public String getErrorCode() {
        return "INVALID_USER_ID";
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
