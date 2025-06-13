package rental.rentallistingservice.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rental.rentallistingservice.Model.UserWatchList;

import java.util.Optional;

@Repository
public interface UserWatchlistRepository extends JpaRepository<UserWatchList, Long> {
    Optional<UserWatchList> findByUserId(Long userId);
}
