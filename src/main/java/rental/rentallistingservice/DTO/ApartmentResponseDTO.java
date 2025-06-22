package rental.rentallistingservice.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "DTO do przysyłania informacji o mieszkaniu")
public class ApartmentResponseDTO {
    @Schema(description = "Unikalny identyfikator mieszkania", example = "1")
    private Long id;

    @Schema(description = "Cena mieszkania", example = "2500.00")
    private BigDecimal price;

    @Schema(description = "Lokalizacja mieszkania", example = "Warszawa")
    private String location;

    @Schema(description = "Liczba pokoi w mieszkaniu", example = "3")
    private Integer rooms;

    @Schema(description = "Typ najmu", example = "LONG_TERM")
    private String rentalType;

    @Schema(description = "Dostępność mieszkania", example = "true")
    private Boolean available;

    @Schema(description = "Szerokość geograficzna", example = "52.2297")
    private Double latitude;

    @Schema(description = "Długość geograficzna", example = "21.0122")
    private Double longitude;

    @Schema(description = "ID właściciela mieszkania", example = "15")
    private Long ownerId;

    @Schema(description = "Imię właściciela mieszkania", example = "Anna")
    private String ownerName;

    @Schema(description = "Średnia ocena mieszkania", example = "4.5")
    private BigDecimal averageRating;

    @Schema(description = "Liczba wystawionych ocen", example = "12")
    private Integer ratingCount;

    @Schema(description = "Liczba wyświetleń ogłoszenia", example = "135")
    private Long viewCount;

}