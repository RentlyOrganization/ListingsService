package rental.rentallistingservice.Controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rental.rentallistingservice.DTO.ApartmentResponseDTO;
import rental.rentallistingservice.DTO.CreateApartmentDTO;
import rental.rentallistingservice.DTO.UpdateApartmentDTO;
import rental.rentallistingservice.DTO.UserDTO;
import rental.rentallistingservice.Exceptions.*;
import rental.rentallistingservice.Mapper.ApartmentMapper;
import rental.rentallistingservice.Model.Apartment;
import rental.rentallistingservice.Model.RentalType;
import rental.rentallistingservice.Services.ApartmentService;
import rental.rentallistingservice.microservices.UserOwner;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api/apartments")
@Tag(name = "Apartment Controller", description = "Kontroler zarządzający ofertami mieszkań")

public class ApartmentController {

    private static final Set<String> ALLOWED_SEARCH_PARAMS = Set.of(
            "minPrice", "maxPrice", "location", "minRooms",
            "rentalType", "available", "latitude", "longitude", "radius"
    );

    private final UserOwner userOwner;
    private final ApartmentService apartmentService;
    private final ApartmentMapper apartmentMapper;

    @Autowired
    public ApartmentController(UserOwner userOwner, ApartmentService apartmentService, ApartmentMapper apartmentMapper) {
        this.apartmentService = apartmentService;
        this.apartmentMapper= apartmentMapper;
        this.userOwner = userOwner;
    }

    @Operation(summary = "Dodaj mieszkanie",
            description = "Dodaje nową ofertę mieszkania do systemu")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pomyślnie dodano mieszkanie"),
            @ApiResponse(responseCode = "400", description = "Nieprawidłowe dane mieszkania"),
            @ApiResponse(responseCode = "404", description = "Nie znaleziono użytkownika"),
            @ApiResponse(responseCode = "500", description = "Wewnętrzny błąd serwera")
    })
    @PostMapping
    public ResponseEntity<ApartmentResponseDTO> addApartment(
            @Parameter(description = "Dane mieszkania") @Valid @RequestBody CreateApartmentDTO apartmentDto) {

        if (apartmentDto == null) {
            throw new InvalidParameterException("Dane mieszkania nie mogą być puste");
        }

        if (apartmentDto.getPrice() == null || apartmentDto.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidPriceRangeException("Cena mieszkania musi być większa od zera");
        }

        validateOwner(apartmentDto.getOwnerId());

        validateRoomsNumber(apartmentDto.getRooms());
        validateRentalType(apartmentDto.getRentalType());
        validateLocation(apartmentDto.getLocation());
        validateCoordinates(apartmentDto.getLatitude(), apartmentDto.getLongitude());

        Apartment apartment = apartmentMapper.toEntity(apartmentDto);
        Apartment savedApartment = apartmentService.save(apartment);
        return ResponseEntity.ok(mapToResponseDTOWithOwner(savedApartment));

    }

    @Operation(summary = "Pobierz wszystkie mieszkania",
            description = "Zwraca listę wszystkich dostępnych mieszkań")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pomyślnie pobrano listę mieszkań"),
            @ApiResponse(responseCode = "500", description = "Wewnętrzny błąd serwera")
    })
    @GetMapping
    public ResponseEntity<List<ApartmentResponseDTO>> getAllApartments(
            @RequestParam Map<String, String> allParams
    ) {
        if (!allParams.isEmpty()) {
            throw new InvalidParameterException("Endpoint nie przyjmuje żadnych parametrów zapytania");
        }

        List<Apartment> apartments = apartmentService.getAll();
        List<ApartmentResponseDTO> apartmentDTOs = apartments.stream()
                .map(this::mapToResponseDTOWithOwner)
                .toList();
        return ResponseEntity.ok(apartmentDTOs);
    }

    @Operation(summary = "Wyszukaj mieszkania",
            description = "Wyszukuje mieszkania według zadanych kryteriów")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pomyślnie wyszukano mieszkania"),
            @ApiResponse(responseCode = "400", description = "Nieprawidłowe parametry wyszukiwania"),
            @ApiResponse(responseCode = "500", description = "Wewnętrzny błąd serwera")
    })
    @GetMapping("/search")
    public ResponseEntity<List<ApartmentResponseDTO>> searchApartments(
            @RequestParam Map<String, String> allParams,
            @Parameter(description = "Minimalna cena") @RequestParam(required = false) BigDecimal minPrice,
            @Parameter(description = "Maksymalna cena") @RequestParam(required = false) BigDecimal maxPrice,
            @Parameter(description = "Lokalizacja") @RequestParam(required = false) String location,
            @Parameter(description = "Minimalna liczba pokoi") @RequestParam(required = false) Integer minRooms,
            @Parameter(description = "Typ najmu") @RequestParam(required = false) String rentalType,
            @Parameter(description = "Dostępność") @RequestParam(required = false) Boolean available,
            @Parameter(description = "Szerokość geograficzna") @RequestParam(required = false) Double latitude,
            @Parameter(description = "Długość geograficzna") @RequestParam(required = false) Double longitude,
            @Parameter(description = "Promień wyszukiwania (km)") @RequestParam(required = false) Double radius

    )  {
        for (String param : allParams.keySet()) {
            if (!ALLOWED_SEARCH_PARAMS.contains(param)) {
                throw new InvalidParameterException("Niedozwolony parametr: " + param);
            }
        }

        validatePriceRange(minPrice, maxPrice);
        validateRoomsNumber(minRooms);
        validateRentalType(rentalType);
        validateRadius(radius);
        validateLocation(location);
        validateCoordinates(latitude, longitude);
        validateSearchParameters(latitude, longitude, radius);
        List<Apartment> results = apartmentService.search(minPrice, maxPrice, location, minRooms, rentalType, available,
                latitude, longitude, radius);
        List<ApartmentResponseDTO> dtos = results.stream()
                .map(apartmentMapper::toResponseDTO)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    @Operation(summary = "Pobierz mieszkanie",
            description = "Zwraca szczegóły mieszkania o podanym ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pomyślnie pobrano mieszkanie"),
            @ApiResponse(responseCode = "404", description = "Nie znaleziono mieszkania"),
            @ApiResponse(responseCode = "500", description = "Wewnętrzny błąd serwera")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ApartmentResponseDTO> getApartmentById(
            @Parameter(description = "ID mieszkania") @PathVariable Long id) {
        if (id == null || id <= 0) {
            throw new InvalidParameterException("ID mieszkania musi być większe od zera");
        }

        Apartment apartment = apartmentService.incrementViewCount(id);


        return ResponseEntity.ok(mapToResponseDTOWithOwner(apartment));
    }

    @Operation(summary = "Dodaj ocenę mieszkania",
              description = "Dodaje nową ocenę do mieszkania i aktualizuje średnią")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pomyślnie dodano ocenę"),
            @ApiResponse(responseCode = "400", description = "Nieprawidłowa ocena"),
            @ApiResponse(responseCode = "404", description = "Nie znaleziono mieszkania"),
            @ApiResponse(responseCode = "500", description = "Wewnętrzny błąd serwera")
    })
    @PostMapping("/{id}/rating")
    public ResponseEntity<ApartmentResponseDTO> addRating(
            @Parameter(description = "ID mieszkania") @PathVariable Long id,
            @Parameter(description = "Ocena (1-5)") @RequestParam BigDecimal rating) {

        if (id == null || id <= 0) {
            throw new InvalidParameterException("ID mieszkania musi być większe od zera");
        }

        if (rating == null || rating.compareTo(BigDecimal.valueOf(1)) < 0 || rating.compareTo(BigDecimal.valueOf(5)) > 0) {
            throw new InvalidParameterException("Ocena musi być w zakresie od 1 do 5");
        }

        Apartment updatedApartment = apartmentService.addRating(id, rating);
        return ResponseEntity.ok(mapToResponseDTOWithOwner(updatedApartment));
    }

    @Operation(summary = "Pobierz statystyki mieszkania",
            description = "Zwraca statystyki mieszkania (średnia ocena, liczba ocen, liczba wyświetleń)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pomyślnie pobrano statystyki"),
            @ApiResponse(responseCode = "404", description = "Nie znaleziono mieszkania"),
            @ApiResponse(responseCode = "500", description = "Wewnętrzny błąd serwera")
    })
    @GetMapping("/{id}/stats")
    public ResponseEntity<Map<String, Object>> getApartmentStats(
            @Parameter(description = "ID mieszkania") @PathVariable Long id) {

        if (id == null || id <= 0) {
            throw new InvalidParameterException("ID mieszkania musi być większe od zera");
        }

        Apartment apartment = apartmentService.getAll().stream()
                .filter(a -> a.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new ApartmentNotFoundException("Mieszkanie o ID " + id + " nie zostało znalezione"));

        Map<String, Object> stats = Map.of(
                "apartmentId", apartment.getId(),
                "averageRating", apartment.getAverageRating(),
                "ratingCount", apartment.getRatingCount(),
                "viewCount", apartment.getViewCount(),
                "totalRating", apartment.getTotalRating()
        );

        return ResponseEntity.ok(stats);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApartmentResponseDTO> updateApartment(
            @PathVariable Long id,
            @RequestBody UpdateApartmentDTO updateDto
    ) {
        if (id == null || id <= 0) {
            throw new InvalidParameterException("ID mieszkania musi być większe od zera");
        }

        Apartment apartment = apartmentService.getAll().stream()
                .filter(a -> a.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new ApartmentNotFoundException("Mieszkanie o ID " + id + " nie zostało znalezione"));

        if (updateDto.getOwnerId() != null) validateOwner(updateDto.getOwnerId());
        if (updateDto.getRooms() != null) validateRoomsNumber(updateDto.getRooms());
        if (updateDto.getRentalType() != null) validateRentalType(updateDto.getRentalType());
        if (updateDto.getLocation() != null) validateLocation(updateDto.getLocation());
        if (updateDto.getLatitude() != null || updateDto.getLongitude() != null)
            validateCoordinates(updateDto.getLatitude(), updateDto.getLongitude());
        if (updateDto.getPrice() != null) validatePrice(updateDto.getPrice());

        if (updateDto.getPrice() != null) apartment.setPrice(updateDto.getPrice());
        if (updateDto.getLocation() != null) apartment.setLocation(updateDto.getLocation());
        if (updateDto.getRooms() != null) apartment.setRooms(updateDto.getRooms());
        if (updateDto.getRentalType() != null) apartment.setRentalType(RentalType.valueOf(updateDto.getRentalType()));
        if (updateDto.getAvailable() != null) apartment.setAvailable(updateDto.getAvailable());
        if (updateDto.getLatitude() != null) apartment.setLatitude(updateDto.getLatitude());
        if (updateDto.getLongitude() != null) apartment.setLongitude(updateDto.getLongitude());
        if (updateDto.getOwnerId() != null) apartment.setOwnerId(updateDto.getOwnerId());

        Apartment updated = apartmentService.save(apartment);
        return ResponseEntity.ok(mapToResponseDTOWithOwner(updated));
    }

    private void validateOwner(Long ownerId) {
        try {
            UserDTO user = userOwner.getUserById(ownerId);
            if (user == null) {
                throw new InvalidParameterException("Nie znaleziono użytkownika o ID: " + ownerId);
            }
        } catch (Exception e) {
            throw new InvalidParameterException("Błąd podczas weryfikacji użytkownika: " + e.getMessage());
        }
    }

    private void validatePrice(BigDecimal price) {
        if (price == null || price.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidPriceRangeException("Cena mieszkania musi być większa od zera");
        }
    }

    private void validatePriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        if (minPrice != null && minPrice.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidPriceRangeException("Minimalna cena nie może być ujemna");
        }
        if (maxPrice != null && maxPrice.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidPriceRangeException("Maksymalna cena nie może być ujemna");
        }
        if (minPrice != null && maxPrice != null && minPrice.compareTo(maxPrice) > 0) {
            throw new InvalidPriceRangeException("Minimalna cena nie może być większa od maksymalnej");
        }
    }

    private void validateRentalType(String rentalType) {
        if (rentalType != null && !List.of("SHORT_TERM", "LONG_TERM", "DAILY").contains(rentalType)) {
            throw new InvalidRentalTypeException("Nieprawidłowy typ najmu: " + rentalType);
        }
    }

    private void validateRadius(Double radius) {
        if (radius != null && (radius <= 0 || radius > 100)) {
            throw new InvalidRadiusException("Promień wyszukiwania musi być dodatni i nie większy niż 100 km");
        }
    }

    private void validateRoomsNumber(Integer minRooms) {
        if (minRooms != null && minRooms <= 0) {
            throw new InvalidRoomsNumberException("Liczba pokoi musi być większa od zera");
        }
    }

    private void validateLocation(String location) {
        if (location != null && location.trim().isEmpty()) {
            throw new InvalidLocationException("Lokalizacja nie może być pustym ciągiem znaków");
        }
    }

    private void validateCoordinates(Double latitude, Double longitude) {
        if (latitude != null && (latitude < -90 || latitude > 90)) {
            throw new InvalidCoordinatesException("Szerokość geograficzna musi być w zakresie od -90 do 90 stopni");
        }
        if (longitude != null && (longitude < -180 || longitude > 180)) {
            throw new InvalidCoordinatesException("Długość geograficzna musi być w zakresie od -180 do 180 stopni");
        }
        if ((latitude != null && longitude == null) || (latitude == null && longitude != null)) {
            throw new InvalidCoordinatesException("Obie współrzędne geograficzne muszą być podane jednocześnie");
        }
    }

    private void validateSearchParameters(Double latitude, Double longitude, Double radius) {
        if (radius != null && (latitude == null || longitude == null)) {
            throw new InvalidCoordinatesException("Promień wyszukiwania wymaga podania współrzędnych geograficznych");
        }
    }

    private ApartmentResponseDTO mapToResponseDTOWithOwner(Apartment apartment) {
        ApartmentResponseDTO dto = apartmentMapper.toResponseDTO(apartment);

        try {
            UserDTO owner = userOwner.getUserById(apartment.getOwnerId());
            if (owner != null) {
                dto.setOwnerId(owner.getId());
                dto.setOwnerName(owner.getFirstName());
            }
        } catch (Exception e) {
            dto.setOwnerId(apartment.getOwnerId());
            dto.setOwnerName("Nieznany");
        }

        return dto;
    }


}
