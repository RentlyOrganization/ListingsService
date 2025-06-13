package rental.rentallistingservice.Exceptions;

public sealed interface ApplicationException
    permits ValidationException, NotFoundException{
    String getErrorCode();
    String getMessage();
}


