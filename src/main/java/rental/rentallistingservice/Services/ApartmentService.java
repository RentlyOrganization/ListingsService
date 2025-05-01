package rental.rentallistingservice.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rental.rentallistingservice.Model.Apartment;
import rental.rentallistingservice.Repositories.ApartmentRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ApartmentService {
    @Autowired
    private ApartmentRepository apartmentRepository;

    public Apartment save(Apartment apartment) {
        return apartmentRepository.save(apartment);
    }

    public List<Apartment> getAll() {
        return apartmentRepository.findAll();
    }

    public List<Apartment> search(BigDecimal minPrice, BigDecimal maxPrice, String location,
                                  Integer minRooms, String rentalType, Boolean available) {
        return apartmentRepository.findAll().stream()
                .filter(a -> minPrice == null || a.getPrice().compareTo(minPrice) >= 0)
                .filter(a -> maxPrice == null || a.getPrice().compareTo(maxPrice) <= 0)
                .filter(a -> location == null || a.getLocation().toLowerCase().contains(location.toLowerCase()))
                .filter(a -> minRooms == null || a.getRooms() >= minRooms)
                .filter(a -> rentalType == null || (a.getRentalType() != null && a.getRentalType().name().equalsIgnoreCase(rentalType)))
                .filter(a -> available == null || a.isAvailable() == available)
                .collect(Collectors.toList());
    }

}
