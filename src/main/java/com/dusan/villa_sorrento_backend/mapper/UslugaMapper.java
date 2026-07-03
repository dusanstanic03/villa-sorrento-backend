package com.dusan.villa_sorrento_backend.mapper;

import com.dusan.villa_sorrento_backend.dto.UslugaDTO;
import com.dusan.villa_sorrento_backend.model.Usluga;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

/**
 * Mapper za konverziju izmedju Usluga entiteta i DTO objekta.
 */
@Mapper(componentModel = "spring")
public interface UslugaMapper {
    UslugaDTO uslugaToUslugaDTO(Usluga usluga);

    @Mapping(target = "stavkeRezervacije", ignore = true)
    Usluga uslugaDTOToUsluga(UslugaDTO uslugaDTO);

    @Mapping(target = "idUsluga", ignore = true)
    @Mapping(target = "stavkeRezervacije", ignore = true)
    void updateUslugaFromDto(UslugaDTO uslugaDTO, @MappingTarget Usluga usluga);
}
