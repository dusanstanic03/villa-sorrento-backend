/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.dusan.villa_sorrento_backend.mapper;

/**
 *
 * @author Dusan
 */


import com.dusan.villa_sorrento_backend.dto.StavkaRezervacijeDTO;
import com.dusan.villa_sorrento_backend.model.StavkaRezervacije;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface StavkaRezervacijeMapper {
    StavkaRezervacijeMapper INSTANCE = Mappers.getMapper(StavkaRezervacijeMapper.class);

    @Mapping(source = "soba.idSoba", target = "sobaId") //entitet ima Soba soba a DTP Long sobaId, pa nam treba samo idSoba 
    StavkaRezervacijeDTO stavkaRezervacijeToStavkaRezervacijeDTO(StavkaRezervacije stavkaRezervacije);

    @Mapping(target = "rezervacija", ignore = true) // Rezervacija će se setovati u servisu
    @Mapping(target = "soba", ignore = true) // Soba će se setovati u servisu
    StavkaRezervacije stavkaRezervacijeDTOToStavkaRezervacije(StavkaRezervacijeDTO stavkaRezervacijeDTO);
}
