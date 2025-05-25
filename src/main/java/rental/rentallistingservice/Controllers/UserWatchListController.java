package rental.rentallistingservice.Controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import rental.rentallistingservice.Services.UserWatchListService;

import java.util.List;

@Controller
@RequestMapping("/api/watchlist")
public class UserWatchListController {
    @Autowired
    private UserWatchListService userWatchlistService;

    @GetMapping
    public ResponseEntity<List<Long>> getWatchedApartments(
            @RequestParam String userId) {
        return ResponseEntity.ok(userWatchlistService.getWatchedApartmentIds(userId));
    }

    @PostMapping("/{apartmentId}")
    public ResponseEntity<Void> addToWatchlist(
            @PathVariable Long apartmentId,
            @RequestParam String userId) {
        userWatchlistService.addToWatchlist(userId, apartmentId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{apartmentId}")
    public ResponseEntity<Void> removeFromWatchlist(
            @PathVariable Long apartmentId,
            @RequestParam String userId) {
        userWatchlistService.removeFromWatchlist(userId, apartmentId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/check/{apartmentId}")
    public ResponseEntity<Boolean> isWatched(
            @PathVariable Long apartmentId,
            @RequestParam String userId) {
        return ResponseEntity.ok(userWatchlistService.isWatched(userId, apartmentId));
    }
}
