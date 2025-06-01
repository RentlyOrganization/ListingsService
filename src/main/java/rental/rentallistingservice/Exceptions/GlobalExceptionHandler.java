package rental.rentallistingservice.Exceptions;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(InvalidRoomsNumberException.class)
    @ApiResponse(responseCode = "400", description = "Nieprawidłowa liczba pokoi",
            content = @Content(schema = @Schema(implementation = String.class)))
    public ResponseEntity<String> handleInvalidRoomsNumber(InvalidRoomsNumberException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidParameterException.class)
    @ApiResponse(responseCode = "400", description = "Nieprawidłowy parametr zapytania",
            content = @Content(schema = @Schema(implementation = String.class)))
    public ResponseEntity<String> handleInvalidParameter(InvalidParameterException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidLocationException.class)
    @ApiResponse(responseCode = "400", description = "Nieprawidłowa lokalizacja",
            content = @Content(schema = @Schema(implementation = String.class)))
    public ResponseEntity<String> handleInvalidLocation(InvalidLocationException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidCoordinatesException.class)
    @ApiResponse(responseCode = "400", description = "Nieprawidłowe współrzędne geograficzne",
            content = @Content(schema = @Schema(implementation = String.class)))
    public ResponseEntity<String> handleInvalidCoordinates(InvalidCoordinatesException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidPriceRangeException.class)
    @ApiResponse(responseCode = "400", description = "Nieprawidłowy zakres cenowy",
            content = @Content(schema = @Schema(implementation = String.class)))
    public ResponseEntity<String> handleInvalidPriceRange(InvalidPriceRangeException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidRentalTypeException.class)
    @ApiResponse(responseCode = "400", description = "Nieprawidłowy typ najmu",
            content = @Content(schema = @Schema(implementation = String.class)))
    public ResponseEntity<String> handleInvalidRentalType(InvalidRentalTypeException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidRadiusException.class)
    @ApiResponse(responseCode = "400", description = "Nieprawidłowy promień wyszukiwania",
            content = @Content(schema = @Schema(implementation = String.class)))
    public ResponseEntity<String> handleInvalidRadius(InvalidRadiusException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ApartmentNotFoundException.class)
    @ApiResponse(responseCode = "404", description = "Nie znaleziono mieszkania",
            content = @Content(schema = @Schema(implementation = String.class)))
    public ResponseEntity<String> handleApartmentNotFound(ApartmentNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidApartmentDataException.class)
    @ApiResponse(responseCode = "400", description = "Nieprawidłowe dane mieszkania",
            content = @Content(schema = @Schema(implementation = String.class)))
    public ResponseEntity<String> handleInvalidApartmentData(InvalidApartmentDataException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    @ApiResponse(responseCode = "500", description = "Wewnętrzny błąd serwera",
            content = @Content(schema = @Schema(implementation = String.class)))
    public ResponseEntity<String> handleGenericException(Exception ex) {
        return new ResponseEntity<>("Wystąpił nieoczekiwany błąd: " + ex.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ApiResponse(responseCode = "404", description = "Nie znaleziono użytkownika",
            content = @Content(schema = @Schema(implementation = String.class)))
    public ResponseEntity<String> handleUserNotFound(UserNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidApartmentIdException.class)
    @ApiResponse(responseCode = "400", description = "Nieprawidłowe ID mieszkania",
            content = @Content(schema = @Schema(implementation = String.class)))
    public ResponseEntity<String> handleInvalidApartmentId(InvalidApartmentIdException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidUserIdException.class)
    @ApiResponse(responseCode = "400", description = "Nieprawidłowe ID użytkownika",
            content = @Content(schema = @Schema(implementation = String.class)))
    public ResponseEntity<String> handleInvalidUserId(InvalidUserIdException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }



    @ExceptionHandler(WatchlistEntryNotFoundException.class)
    @ApiResponse(responseCode = "404", description = "Nie znaleziono wpisu na liście obserwowanych",
            content = @Content(schema = @Schema(implementation = String.class)))
    public ResponseEntity<String> handleWatchlistEntryNotFound(WatchlistEntryNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

}
