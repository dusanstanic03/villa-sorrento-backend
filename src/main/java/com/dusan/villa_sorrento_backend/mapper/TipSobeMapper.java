package com.dusan.villa_sorrento_backend.mapper;

import com.dusan.villa_sorrento_backend.dto.TipSobeDTO;
import com.dusan.villa_sorrento_backend.model.TipSobe;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

/**
 * Mapper za konverziju izmedju TipSobe entiteta i DTO objekta.
 */
@Mapper(componentModel = "spring")
public interface TipSobeMapper {
    TipSobeDTO tipSobeToTipSobeDTO(TipSobe tipSobe);

    TipSobe tipSobeDTOToTipSobe(TipSobeDTO tipSobeDTO);

    void updateTipSobeFromDto(TipSobeDTO tipSobeDTO, @MappingTarget TipSobe tipSobe);
}
