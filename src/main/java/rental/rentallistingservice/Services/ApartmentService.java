package rental.rentallistingservice.Services;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rental.rentallistingservice.Model.Apartment;
import rental.rentallistingservice.Repositories.ApartmentRepository;

import java.util.List;

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
}
