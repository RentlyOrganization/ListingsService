package rental.rentallistingservice.microservices;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import rental.rentallistingservice.DTO.UserDTO;

@FeignClient(name = "RENTLYAUTH")
public interface UserOwner {
    @GetMapping("/findUserByID/{userId}")
    UserDTO getUserById(@PathVariable Long userId);
}