/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.dusan.villa_sorrento_backend.mapper;


import com.dusan.villa_sorrento_backend.dto.SobaDTO;
import com.dusan.villa_sorrento_backend.model.Soba;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

/**
 *
 * @author Dusan
 */
@Mapper(componentModel = "spring")
public interface SobaMapper {

    @Mapping(source = "tipSobe.idTipSobe", target = "tipSobeId")
    @Mapping(source = "tipSobe.naziv", target = "tipSobeNaziv")
    SobaDTO sobaToSobaDTO(Soba soba);
    
    @Mapping(target = "stavkeRezervacije", ignore = true)
    @Mapping(target = "tipSobe", ignore = true)
    Soba sobaDTOToSoba(SobaDTO sobaDTO);

    @Mapping(target = "idSoba", ignore = true)
    @Mapping(target = "stavkeRezervacije", ignore = true)
    @Mapping(target = "tipSobe", ignore = true)
    void updateSobaFromDto(SobaDTO sobaDTO, @MappingTarget Soba soba);
}
