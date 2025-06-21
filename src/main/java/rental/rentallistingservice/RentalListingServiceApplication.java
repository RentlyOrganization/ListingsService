package rental.rentallistingservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan("rental.rentallistingservice.Model")
@EnableDiscoveryClient
@EnableJpaRepositories(basePackages = "rental.rentallistingservice.Repositories")
@EnableFeignClients(basePackages = "rental.rentallistingservice.microservices")
public class RentalListingServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(RentalListingServiceApplication.class, args);
    }

}
