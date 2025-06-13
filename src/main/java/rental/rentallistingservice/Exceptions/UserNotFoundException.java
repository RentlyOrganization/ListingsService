package rental.rentallistingservice.Exceptions;

public final class UserNotFoundException extends RuntimeException implements NotFoundException{
    public UserNotFoundException(String message) {
        super(message);
    }

    @Override
    public String getErrorCode() {
        return "USER_NOT_FOUND";
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
