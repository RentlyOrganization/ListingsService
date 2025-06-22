package rental.rentallistingservice.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Schema(description = "DTO dla aktualizacji danych mieszkania")
public class UpdateApartmentDTO {
    @DecimalMin(value = "0.0", inclusive = false, message = "Cena musi być większa od 0")
    @Schema(description = "Cena mieszkania", example = "2500.00")
    private BigDecimal price;

    @NotBlank(message = "Lokalizacja jest wymagana")
    @Schema(description = "Lokalizacja mieszkania", example = "Warszawa")
    private String location;

    @Schema(description = "Liczba pokoi", example = "2")
    @Positive(message = "Liczba pokoi musi być większa od zera")
    private Integer rooms;

    @Schema(description = "Typ najmu", example = "LONG_TERM")
    @Pattern(regexp = "SHORT_TERM|LONG_TERM|DAILY", message = "Nieprawidłowy typ najmu")
    private String rentalType;

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

    @Positive(message = "OwnerId musi być większy od zera")
    @Schema(description = "ID właściciela", example = "1")
    private Long ownerId;
}