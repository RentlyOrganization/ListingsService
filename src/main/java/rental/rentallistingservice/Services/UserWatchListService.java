package rental.rentallistingservice.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rental.rentallistingservice.Model.UserWatchList;
import rental.rentallistingservice.Repositories.UserWatchlistRepository;

import java.util.List;

@Service
public class UserWatchListService {

    private final UserWatchlistRepository userWatchlistRepository;

    @Autowired
    public UserWatchListService(UserWatchlistRepository userWatchlistRepository) {
        this.userWatchlistRepository = userWatchlistRepository;
    }

    public List<Long> getWatchedApartmentIds(Long userId) {
        return userWatchlistRepository.findByUserId(userId)
                .map(UserWatchList::getWatchedApartmentIds)
                .orElse(List.of());
    }

    public void addToWatchlist(Long userId, Long apartmentId) {
        UserWatchList watchlist = userWatchlistRepository.findByUserId(userId)
                .orElse(new UserWatchList());

        if (watchlist.getUserId() == null) {
            watchlist.setUserId(userId);
        }

        if (!watchlist.getWatchedApartmentIds().contains(apartmentId)) {
            watchlist.getWatchedApartmentIds().add(apartmentId);
            userWatchlistRepository.save(watchlist);
        }
    }

    public void removeFromWatchlist(Long userId, Long apartmentId) {
        userWatchlistRepository.findByUserId(userId).ifPresent(watchlist -> {
            watchlist.getWatchedApartmentIds().remove(apartmentId);
            userWatchlistRepository.save(watchlist);
        });
    }

    public boolean isWatched(Long userId, Long apartmentId) {
        return userWatchlistRepository.findByUserId(userId)
                .map(watchlist -> watchlist.getWatchedApartmentIds().contains(apartmentId))
                .orElse(false);
    }
}
