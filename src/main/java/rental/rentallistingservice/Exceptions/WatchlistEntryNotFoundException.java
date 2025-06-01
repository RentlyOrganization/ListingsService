package rental.rentallistingservice.Exceptions;

public class WatchlistEntryNotFoundException extends RuntimeException {
    public WatchlistEntryNotFoundException(String message) {
        super(message);
    }
}
