package com.dusan.villa_sorrento_backend.mapper;

import com.dusan.villa_sorrento_backend.dto.GostDTO;
import com.dusan.villa_sorrento_backend.model.Gost;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

/**
 * Mapper za konverziju izmedju Gost entiteta i DTO objekta.
 */
@Mapper(componentModel = "spring")
public interface GostMapper {
    GostDTO gostToGostDTO(Gost gost);

    @Mapping(target = "stavkeRezervacije", ignore = true)
    Gost gostDTOToGost(GostDTO gostDTO);

    @Mapping(target = "idGost", ignore = true)
    @Mapping(target = "stavkeRezervacije", ignore = true)
    void updateGostFromDto(GostDTO gostDTO, @MappingTarget Gost gost);
}
