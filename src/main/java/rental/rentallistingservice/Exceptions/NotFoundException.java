package rental.rentallistingservice.Exceptions;

public sealed interface NotFoundException extends ApplicationException
    permits UserNotFoundException,
            ApartmentNotFoundException,
            WatchlistEntryNotFoundException{}
