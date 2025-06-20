package rental.rentallistingservice.Exceptions;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CustomErrorResponse> handleValidationExceptions(
            MethodArgumentNotValidException ex, WebRequest request) {
        String errorMessage = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));

        return new ResponseEntity<>(
                new CustomErrorResponse(
                        LocalDateTime.now(),
                        HttpStatus.BAD_REQUEST.value(),
                        "VALIDATION_ERROR",
                        errorMessage,
                        request.getDescription(false)
                ),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(Exception.class)
    @ApiResponse(responseCode = "400", description = "Błąd aplikacji",
            content = @Content(schema = @Schema(implementation = CustomErrorResponse.class)))
    public ResponseEntity<CustomErrorResponse> handleException(Exception ex, WebRequest request) {
        return switch (ex) {
            case ValidationException ve -> new ResponseEntity<>(
                    new CustomErrorResponse(
                            LocalDateTime.now(),
                            HttpStatus.BAD_REQUEST.value(),
                            ve.getErrorCode(),
                            ve.getMessage(),
                            request.getDescription(false)
                    ),
                    HttpStatus.BAD_REQUEST
            );

            case NotFoundException nfe -> new ResponseEntity<>(
                    new CustomErrorResponse(
                            LocalDateTime.now(),
                            HttpStatus.NOT_FOUND.value(),
                            nfe.getErrorCode(),
                            nfe.getMessage(),
                            request.getDescription(false)
                    ),
                    HttpStatus.NOT_FOUND
            );

            default -> new ResponseEntity<>(
                    new CustomErrorResponse(
                            LocalDateTime.now(),
                            HttpStatus.INTERNAL_SERVER_ERROR.value(),
                            "INTERNAL_ERROR",
                            ex.getMessage(),
                            request.getDescription(false)
                    ),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        };
    }
}
