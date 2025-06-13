package rental.rentallistingservice.Exceptions;

public final class InvalidPriceRangeException extends RuntimeException implements ValidationException{
    public InvalidPriceRangeException(String message) {
        super(message);
    }

    @Override
    public String getErrorCode() {
        return "INVALID_PRICE_RANGE";
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
