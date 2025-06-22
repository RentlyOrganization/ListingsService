package rental.rentallistingservice;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import rental.rentallistingservice.Exceptions.ApartmentNotFoundException;
import rental.rentallistingservice.Model.Apartment;
import rental.rentallistingservice.Model.RentalType;
import rental.rentallistingservice.Repositories.ApartmentRepository;
import rental.rentallistingservice.Services.ApartmentService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ApartmentServiceTest {

    private ApartmentService apartmentService;
    private ApartmentRepository apartmentRepository;

    @BeforeEach
    void setUp() {
        apartmentRepository = org.mockito.Mockito.mock(ApartmentRepository.class);
        apartmentService = new ApartmentService(apartmentRepository);
    }

    @Test
    void testSearchApartmentsWithFilters() {
        Apartment a1 = new Apartment();
        a1.setPrice(new BigDecimal("1000"));
        a1.setLocation("Warszawa");
        a1.setRooms(2);
        a1.setRentalType(RentalType.LONG_TERM);
        a1.setAvailable(true);

        Apartment a2 = new Apartment();
        a2.setPrice(new BigDecimal("3000"));
        a2.setLocation("Kraków");
        a2.setRooms(1);
        a2.setRentalType(RentalType.SHORT_TERM);
        a2.setAvailable(false);

        when(apartmentRepository.findAll()).thenReturn(List.of(a1, a2));

        List<Apartment> result = apartmentService.search(
                new BigDecimal("500"), new BigDecimal("2000"),
                "warszawa", 2, "LONG_TERM", true, null, null, null
        );

        assertEquals(1, result.size());
        assertEquals("Warszawa", result.getFirst().getLocation());
        assertEquals(new BigDecimal("1000"), result.getFirst().getPrice());
        assertEquals(2, result.getFirst().getRooms());
        assertEquals(RentalType.LONG_TERM, result.getFirst().getRentalType());

    }

    @Test
    void testAddRating() {
        Apartment a = new Apartment();
        a.setId(1L);
        a.setTotalRating(new BigDecimal("8.0"));
        a.setRatingCount(2);

        when(apartmentRepository.findById(1L)).thenReturn(Optional.of(a));
        when(apartmentRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        Apartment updated = apartmentService.addRating(1L, new BigDecimal("4.0"));

        assertEquals(new BigDecimal("12.0"), updated.getTotalRating());
        assertEquals(3, updated.getRatingCount());
        assertEquals(4, updated.getAverageRating().intValue());
    }

    @Test
    void testIncrementViewCount() {
        Apartment a = new Apartment();
        a.setId(1L);
        a.setViewCount(5L);

        when(apartmentRepository.findById(1L)).thenReturn(Optional.of(a));
        when(apartmentRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        Apartment updated = apartmentService.incrementViewCount(1L);

        assertEquals(6, updated.getViewCount());
    }

    @Test
    void testIncrementViewCount_NotFound() {
        when(apartmentRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ApartmentNotFoundException.class, () -> apartmentService.incrementViewCount(1L));
    }
}
