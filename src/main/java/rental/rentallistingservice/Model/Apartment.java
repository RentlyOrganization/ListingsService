package rental.rentallistingservice.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Setter
@Getter
public class Apartment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Positive
    private BigDecimal price;
    @Min(-90)
    @Max(90)
    private Double latitude;
    @Min(-180)
    @Max(180)
    private Double longitude;
    @NotBlank
    private String location;
    @Min(1)
    private int rooms;
    @Enumerated(EnumType.STRING)
    private RentalType rentalType;
    private boolean available;
    @Column(name = "owner_id")
    private Long ownerId;

}
