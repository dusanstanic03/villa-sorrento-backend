package com.dusan.villa_sorrento_backend.mapper;

import com.dusan.villa_sorrento_backend.dto.TipSobeDTO;
import com.dusan.villa_sorrento_backend.model.TipSobe;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

/**
 * Mapper za konverziju izmedju TipSobe entiteta i DTO objekta.
 */
@Mapper(componentModel = "spring")
public interface TipSobeMapper {
    TipSobeDTO tipSobeToTipSobeDTO(TipSobe tipSobe);

    @Mapping(target = "sobe", ignore = true)
    TipSobe tipSobeDTOToTipSobe(TipSobeDTO tipSobeDTO);

    @Mapping(target = "sobe", ignore = true)
    void updateTipSobeFromDto(TipSobeDTO tipSobeDTO, @MappingTarget TipSobe tipSobe);
}
