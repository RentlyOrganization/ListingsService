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
import rental.rentallistingservice.DTO.UserDTO;
import rental.rentallistingservice.Exceptions.*;
import rental.rentallistingservice.Services.ApartmentService;
import rental.rentallistingservice.Services.UserWatchListService;
import rental.rentallistingservice.microservices.UserOwner;

import java.util.List;

@Controller
@RequestMapping("/api/watchlist")
@Tag(name = "Watchlist Controller", description = "Kontroler zarządzający listą obserwowanych mieszkań")
public class UserWatchlistController {

    private final UserWatchListService userWatchlistService;
    private final UserOwner userOwner;
    private final ApartmentService apartmentService;

    @Autowired
    public UserWatchlistController(ApartmentService apartmentService, UserOwner userOwner, UserWatchListService userWatchlistService) {
        this.userWatchlistService = userWatchlistService;
        this.userOwner = userOwner;
        this.apartmentService = apartmentService;
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
        validateUserExists(userId);
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
        validateUserExists(userId);
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
        validateUserExists(userId);
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
        validateUserExists(userId);
        return ResponseEntity.ok(userWatchlistService.isWatched(userId, apartmentId));
    }

    private void validateApartmentId(Long apartmentId) {
        if (apartmentId == null || apartmentId <= 0) {
            throw new InvalidApartmentIdException("ID mieszkania musi być dodatnie");
        }

        apartmentService.getApartmentById(apartmentId);
    }

    private void validateUserExists(Long userId) {
        try {
            UserDTO user = userOwner.getUserById(userId);
            if (user == null) {
                throw new UserNotFoundException("Nie znaleziono użytkownika o ID: " + userId);
            }
        } catch (Exception e) {
            if (e instanceof UserNotFoundException) {
                throw e;
            }
            throw new UserNotFoundException("Błąd podczas weryfikacji użytkownika: " + e.getMessage());
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