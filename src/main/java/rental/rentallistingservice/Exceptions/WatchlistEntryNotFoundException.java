package rental.rentallistingservice.Exceptions;

public final class WatchlistEntryNotFoundException extends RuntimeException implements NotFoundException {
    public WatchlistEntryNotFoundException(String message) {
        super(message);
    }

    @Override
    public String getErrorCode() {
        return "WATCHLIST_ENTRY_NOT_FOUND";
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
