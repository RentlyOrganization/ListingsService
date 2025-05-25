package rental.rentallistingservice.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rental.rentallistingservice.Model.Apartment;
import rental.rentallistingservice.Services.ApartmentService;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/apartments")
public class ApartmentController {
    @Autowired
    private ApartmentService apartmentService;

    @PostMapping
    public ResponseEntity<Apartment> addApartment(@RequestBody Apartment apartment) {
        Apartment savedApartment = apartmentService.save(apartment);
        return ResponseEntity.ok(savedApartment);
    }

    @GetMapping
    public ResponseEntity<List<Apartment>> getAllApartments() {
        List<Apartment> apartments = apartmentService.getAll();
        return ResponseEntity.ok(apartments);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Apartment>> searchApartments(
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) Integer minRooms,
            @RequestParam(required = false) String rentalType,
            @RequestParam(required = false) Boolean available,
            @RequestParam(required = false) Double latitude,
            @RequestParam(required = false) Double longitude,
            @RequestParam(required = false) Double radius
    ) {
        List<Apartment> results = apartmentService.search(minPrice, maxPrice, location, minRooms, rentalType, available,
                latitude, longitude, radius);
        return ResponseEntity.ok(results);
    }

}
