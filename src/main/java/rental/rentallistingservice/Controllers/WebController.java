package rental.rentallistingservice.Controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Tag(name = "Web Controller", description = "Kontroler obsługujący podstawowe widoki strony")
public class WebController {

    @Operation(summary = "Wyświetl stronę główną",
            description = "Zwraca widok strony głównej aplikacji")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pomyślnie załadowano stronę główną"),
            @ApiResponse(responseCode = "404", description = "Nie znaleziono pliku strony")
    })
    @GetMapping("/")
    public String home() {
        return "redirect:/index.html";
    }
}