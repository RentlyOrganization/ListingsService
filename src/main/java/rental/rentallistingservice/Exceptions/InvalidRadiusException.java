package rental.rentallistingservice.Exceptions;

public final class InvalidRadiusException extends RuntimeException implements ValidationException{
  public InvalidRadiusException(String message) {
    super(message);
  }

    @Override
    public String getErrorCode() {
        return "INVALID_RADIUS";
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
