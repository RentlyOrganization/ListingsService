package rental.rentallistingservice.Controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.ws.rs.InternalServerErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rental.rentallistingservice.DTO.ApartmentResponseDTO;
import rental.rentallistingservice.DTO.CreateApartmentDTO;
import rental.rentallistingservice.Exceptions.*;
import rental.rentallistingservice.Mapper.ApartmentMapper;
import rental.rentallistingservice.Model.Apartment;
import rental.rentallistingservice.Services.ApartmentService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/apartments")
@Tag(name = "Apartment Controller", description = "Kontroler zarządzający ofertami mieszkań")

public class ApartmentController {

    private static final Set<String> ALLOWED_SEARCH_PARAMS = Set.of(
            "minPrice", "maxPrice", "location", "minRooms",
            "rentalType", "available", "latitude", "longitude", "radius"
    );

    private final ApartmentService apartmentService;
    private final ApartmentMapper apartmentMapper;

    @Autowired
    public ApartmentController(ApartmentService apartmentService, ApartmentMapper apartmentMapper) {
        this.apartmentService = apartmentService;
        this.apartmentMapper= apartmentMapper;
    }

    @Operation(summary = "Dodaj mieszkanie",
            description = "Dodaje nową ofertę mieszkania do systemu")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pomyślnie dodano mieszkanie"),
            @ApiResponse(responseCode = "400", description = "Nieprawidłowe dane mieszkania"),
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

        validateRoomsNumber(apartmentDto.getRooms());
        validateRentalType(apartmentDto.getRentalType());
        validateLocation(apartmentDto.getLocation());
        validateCoordinates(apartmentDto.getLatitude(), apartmentDto.getLongitude());

        Apartment apartment = apartmentMapper.toEntity(apartmentDto);
        Apartment savedApartment = apartmentService.save(apartment);
        return ResponseEntity.ok(apartmentMapper.toResponseDTO(savedApartment));

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
                .map(apartmentMapper::toResponseDTO)
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

}