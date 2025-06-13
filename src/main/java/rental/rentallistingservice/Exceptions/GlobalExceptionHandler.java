package rental.rentallistingservice.Exceptions;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {
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
