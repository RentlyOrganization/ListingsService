package rental.rentallistingservice.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

@Data
@Schema(description = "DTO do tworzenia nowego mieszkania")
public class CreateApartmentDTO {
    @NotNull(message = "Cena jest wymagana")
    @DecimalMin(value = "0", message = "Cena nie może być ujemna")
    @Schema(description = "Cena mieszkania", example = "2500.00")
    private BigDecimal Price;

    @NotBlank(message = "Lokalizacja jest wymagana")
    @Schema(description = "Lokalizacja mieszkania", example = "Warszawa")
    private String location;

    @NotNull(message = "Liczba pokoi jest wymagana")
    @Min(value = 1, message = "Liczba pokoi musi być większa od zera")
    @Schema(description = "Liczba pokoi", example = "2")
    private Integer Rooms;

    @NotNull(message = "Typ najmu jest wymagany")
    @Pattern(regexp = "SHORT_TERM|LONG_TERM|DAILY", message = "Nieprawidłowy typ najmu")
    @Schema(description = "Typ najmu", example = "LONG_TERM")
    private String rentalType;

    @NotNull(message = "Status dostępności jest wymagany")
    @Schema(description = "Dostępność", example = "true")
    private Boolean available;

    @DecimalMin(value = "-90", message = "Szerokość geograficzna musi być większa lub równa -90")
    @DecimalMax(value = "90", message = "Szerokość geograficzna musi być mniejsza lub równa 90")
    @Schema(description = "Szerokość geograficzna", example = "52.2297")
    private Double latitude;

    @DecimalMin(value = "-180", message = "Długość geograficzna musi być większa lub równa -180")
    @DecimalMax(value = "180", message = "Długość geograficzna musi być mniejsza lub równa 180")
    @Schema(description = "Długość geograficzna", example = "21.0122")
    private Double longitude;

    @NotNull(message = "Id właściciela jest wymagane")
    @Schema(description = "Id właściciela mieszkania", example = "1")
    private Long ownerId;
}
