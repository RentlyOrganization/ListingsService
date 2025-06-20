package rental.rentallistingservice.Mapper;

import org.mapstruct.Mapper;
import rental.rentallistingservice.DTO.ApartmentResponseDTO;
import rental.rentallistingservice.DTO.CreateApartmentDTO;
import rental.rentallistingservice.Model.Apartment;

@Mapper(componentModel = "spring")
public interface ApartmentMapper {
    Apartment toEntity(CreateApartmentDTO dto);
    CreateApartmentDTO toDto(Apartment apartment);
    ApartmentResponseDTO toResponseDTO(Apartment entity);
}