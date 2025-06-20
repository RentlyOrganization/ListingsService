package rental.rentallistingservice.Controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import rental.rentallistingservice.Exceptions.*;
import rental.rentallistingservice.Services.UserWatchListService;

import java.util.List;

@Controller
@RequestMapping("/api/watchlist")
@Tag(name = "Watchlist Controller", description = "Kontroler zarządzający listą obserwowanych mieszkań")
public class UserWatchlistController {

    private final UserWatchListService userWatchlistService;

    @Autowired
    public UserWatchlistController(UserWatchListService userWatchlistService) {
        this.userWatchlistService = userWatchlistService;
    }

    @Operation(summary = "Pobierz listę obserwowanych",
            description = "Zwraca listę ID mieszkań obserwowanych przez użytkownika")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pomyślnie pobrano listę"),
            @ApiResponse(responseCode = "404", description = "Nie znaleziono użytkownika"),
            @ApiResponse(responseCode = "500", description = "Wewnętrzny błąd serwera")
    })
    @GetMapping
    public ResponseEntity<List<Long>> getWatchedApartments(
            @Parameter(description = "ID użytkownika") @RequestParam Long userId) {
        validateUserId(userId);
        return ResponseEntity.ok(userWatchlistService.getWatchedApartmentIds(userId));
    }

    @Operation(summary = "Dodaj do obserwowanych",
            description = "Dodaje mieszkanie do listy obserwowanych")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pomyślnie dodano do listy"),
            @ApiResponse(responseCode = "400", description = "Nieprawidłowe dane"),
            @ApiResponse(responseCode = "404", description = "Nie znaleziono mieszkania lub użytkownika"),
            @ApiResponse(responseCode = "500", description = "Wewnętrzny błąd serwera")
    })
    @PostMapping("/{apartmentId}")
    public ResponseEntity<Void> addToWatchlist(
            @Parameter(description = "ID mieszkania") @PathVariable Long apartmentId,
            @Parameter(description = "ID użytkownika") @RequestParam Long userId) {
        userWatchlistService.addToWatchlist(userId, apartmentId);
        validateApartmentId(apartmentId);
        validateUserId(userId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Usuń z obserwowanych",
            description = "Usuwa mieszkanie z listy obserwowanych")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pomyślnie usunięto z listy"),
            @ApiResponse(responseCode = "404", description = "Nie znaleziono wpisu"),
            @ApiResponse(responseCode = "500", description = "Wewnętrzny błąd serwera")
    })
    @DeleteMapping("/{apartmentId}")
    public ResponseEntity<Void> removeFromWatchlist(
            @Parameter(description = "ID mieszkania") @PathVariable Long apartmentId,
            @Parameter(description = "ID użytkownika") @RequestParam Long userId) {
        validateApartmentId(apartmentId);
        validateUserId(userId);
        userWatchlistService.removeFromWatchlist(userId, apartmentId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Sprawdź czy obserwowane",
            description = "Sprawdza czy mieszkanie jest na liście obserwowanych")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pomyślnie sprawdzono status"),
            @ApiResponse(responseCode = "404", description = "Nie znaleziono użytkownika lub mieszkania"),
            @ApiResponse(responseCode = "500", description = "Wewnętrzny błąd serwera")
    })
    @GetMapping("/check/{apartmentId}")
    public ResponseEntity<Boolean> isWatched(
            @Parameter(description = "ID mieszkania") @PathVariable Long apartmentId,
            @Parameter(description = "ID użytkownika") @RequestParam Long userId) {
        validateApartmentId(apartmentId);
        validateUserId(userId);
        return ResponseEntity.ok(userWatchlistService.isWatched(userId, apartmentId));
    }

    private void validateApartmentId(Long apartmentId) {
        if (apartmentId <= 0) {
            throw new InvalidApartmentIdException("ID mieszkania musi być dodatnie");
        }
    }


    private void validateUserId(Long userId) {
        if (userId == null) {
            throw new InvalidUserIdException("ID użytkownika nie może być puste");
        }
        if (userId <= 0) {
            throw new InvalidUserIdException("ID użytkownika musi być dodatnie");
        }
    }
}