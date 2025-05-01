package rental.rentallistingservice.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rental.rentallistingservice.Model.Apartment;

@Repository
public interface ApartmentRepository extends JpaRepository<Apartment, Long> {
}
