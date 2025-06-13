package rental.rentallistingservice.Exceptions;

public final class ApartmentNotFoundException extends RuntimeException implements NotFoundException {

    public ApartmentNotFoundException(String message) {
        super(message);
    }

    @Override
    public String getErrorCode() {
        return "APARTMENT_NOT_FOUND";
    }

    @Override
    public String getMessage() {
         return super.getMessage();
    }
}
