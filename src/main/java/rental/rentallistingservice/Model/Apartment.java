package rental.rentallistingservice.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
@Getter
@Setter
@NoArgsConstructor
@Entity
public class Apartment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Positive
    private BigDecimal price;
    @NotBlank
    private double latitude;
    @NotBlank
    private double longitude;

    @Min(1)
    private int rooms;
    @Enumerated(EnumType.STRING)
    private RentalType rentalType;
    private boolean available;
}
