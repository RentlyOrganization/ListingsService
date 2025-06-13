package rental.rentallistingservice.Exceptions;

public sealed interface ValidationException extends ApplicationException
        permits InvalidLocationException,
        InvalidParameterException,
        InvalidUserIdException,
        InvalidApartmentIdException,
        InvalidPriceRangeException,
        InvalidApartmentDataException,
        InvalidRadiusException,
        InvalidCoordinatesException,
        InvalidRentalTypeException,
        InvalidRoomsNumberException {}
