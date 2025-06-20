package rental.rentallistingservice.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "DTO do tworzenia nowego mieszkania")
public class CreateApartmentDTO {
    @NotNull(message = "Cena jest wymagana")
    @DecimalMin(value = "0", message = "Cena nie może być ujemna")
    private BigDecimal Price;

    @NotBlank(message = "Lokalizacja jest wymagana")
    private String location;

    @NotNull(message = "Liczba pokoi jest wymagana")
    @Min(value = 1, message = "Liczba pokoi musi być większa od zera")
    private Integer Rooms;

    @NotNull(message = "Typ najmu jest wymagany")
    @Pattern(regexp = "SHORT_TERM|LONG_TERM|DAILY", message = "Nieprawidłowy typ najmu")
    private String rentalType;

    @NotNull(message = "Status dostępności jest wymagany")
    private Boolean available;

    @DecimalMin(value = "-90", message = "Szerokość geograficzna musi być większa lub równa -90")
    @DecimalMax(value = "90", message = "Szerokość geograficzna musi być mniejsza lub równa 90")
    private Double latitude;

    @DecimalMin(value = "-180", message = "Długość geograficzna musi być większa lub równa -180")
    @DecimalMax(value = "180", message = "Długość geograficzna musi być mniejsza lub równa 180")
    private Double longitude;
}