package rental.rentallistingservice.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "DTO do przysyłania informacji o użytkowniku")
public class UserDTO {
    @Schema(description = "ID użytkownika", example = "1")
    private Long id;
    @Schema(description = "Nazwa użytkownika", example = "jan_kowalski")
    private String username;
    @Schema(description = "Email użytkownika", example = "user@example.com")
    private String email;
    @Schema(description = "Imię użytkownika", example = "Jan")
    private String firstName;
    @Schema(description = "Nazwisko użytkownika", example = "Kowalski")
    private String lastName;
}