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
    private Long id;
    private BigDecimal price;
    private String location;
    private Integer rooms;
    private String rentalType;
    private Boolean available;
    private Double latitude;
    private Double longitude;
    private Long ownerId;
    private String ownerName;
    private BigDecimal averageRating;
    private Integer ratingCount;
    private Long viewCount;

}