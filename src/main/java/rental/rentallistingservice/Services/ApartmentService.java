package rental.rentallistingservice.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rental.rentallistingservice.Exceptions.ApartmentNotFoundException;
import rental.rentallistingservice.Model.Apartment;
import rental.rentallistingservice.Repositories.ApartmentRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ApartmentService {

    private final ApartmentRepository apartmentRepository;

    @Autowired
    public ApartmentService(ApartmentRepository apartmentRepository) {
        this.apartmentRepository = apartmentRepository;
    }

    public Apartment save(Apartment apartment) {
        return apartmentRepository.save(apartment);
    }

    public List<Apartment> getAll() {
        return apartmentRepository.findAll();
    }

    public List<Apartment> search(BigDecimal minPrice, BigDecimal maxPrice, String location,
                                  Integer minRooms, String rentalType, Boolean available, Double latitude,
                                  Double longitude, Double radius) {
        List<Apartment> apartments;
        if (latitude != null && longitude != null && radius != null) {
            apartments = apartmentRepository.findApartmentsWithinRadius(latitude, longitude, radius);
        } else {
            apartments = apartmentRepository.findAll();
        }
        return apartments.stream()
                .filter(a -> minPrice == null || a.getPrice().compareTo(minPrice) >= 0)
                .filter(a -> maxPrice == null || a.getPrice().compareTo(maxPrice) <= 0)
                .filter(a -> location == null || a.getLocation().toLowerCase().contains(location.toLowerCase()))
                .filter(a -> minRooms == null || a.getRooms() >= minRooms)
                .filter(a -> rentalType == null || (a.getRentalType() != null && a.getRentalType().name().equalsIgnoreCase(rentalType)))
                .filter(a -> available == null || a.isAvailable() == available)
                .collect(Collectors.toList());
    }

    public Apartment addRating(Long apartmentId, BigDecimal rating) {
        Apartment apartment = apartmentRepository.findById(apartmentId)
                .orElseThrow(() -> new RuntimeException("Nie znaleziono mieszkania o ID: " + apartmentId));

        apartment.setTotalRating(apartment.getTotalRating().add(rating));

        apartment.setRatingCount(apartment.getRatingCount() + 1);

        return apartmentRepository.save(apartment);
    }

    public Apartment incrementViewCount(Long apartmentId) {
        Apartment apartment = apartmentRepository.findById(apartmentId)
                .orElseThrow(() -> new ApartmentNotFoundException("Mieszkanie o ID " + apartmentId + " nie zostało znalezione"));

        apartment.setViewCount(apartment.getViewCount() + 1);
        return apartmentRepository.save(apartment);
    }

    public Apartment getApartmentById(Long apartmentId) {
        return apartmentRepository.findById(apartmentId)
                .orElseThrow(() -> new ApartmentNotFoundException("Mieszkanie o ID " + apartmentId + " nie zostało znalezione"));
    }

}
