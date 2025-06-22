package rental.rentallistingservice.Mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import rental.rentallistingservice.DTO.ApartmentResponseDTO;
import rental.rentallistingservice.DTO.CreateApartmentDTO;
import rental.rentallistingservice.Model.Apartment;

@Mapper(componentModel = "spring")
public interface ApartmentMapper {
    Apartment toEntity(CreateApartmentDTO dto);
    CreateApartmentDTO toDto(Apartment apartment);
    @Mapping(target = "ownerId", source = "ownerId")
    @Mapping(target = "ownerName", ignore = true)
    @Mapping(target = "averageRating", source = "averageRating")
    ApartmentResponseDTO toResponseDTO(Apartment entity);
}