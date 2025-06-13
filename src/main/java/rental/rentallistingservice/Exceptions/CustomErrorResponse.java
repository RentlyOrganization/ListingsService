package rental.rentallistingservice.Exceptions;

import java.time.LocalDateTime;

public record CustomErrorResponse(
        LocalDateTime timestamp,
        int status,
        String error,
        String message,
        String path
) {
}
