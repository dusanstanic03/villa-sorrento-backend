/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.dusan.villa_sorrento_backend.mapper;

/**
 *
 * @author Dusan
 */

import com.dusan.villa_sorrento_backend.dto.RezervacijaDTO;
import com.dusan.villa_sorrento_backend.model.Rezervacija;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.Set;

@Mapper(componentModel = "spring", uses = {StavkaRezervacijeMapper.class}) // Koristi se StavkaRezervacijeMapper zbog seta dole (i entitet i DTO imaju setStav)
public interface RezervacijaMapper {
    RezervacijaMapper INSTANCE = Mappers.getMapper(RezervacijaMapper.class);
    
    @Mapping(source = "user.idUser", target = "userId")
    RezervacijaDTO rezervacijaToRezervacijaDTO(Rezervacija rezervacija);

    @Mapping(target = "user", ignore = true) // User se setuje u servisu
    @Mapping(target = "placanja", ignore = true) // Placanjima se upravljaja posebno
    Rezervacija rezervacijaDTOToRezervacija(RezervacijaDTO rezervacijaDTO);

    // Za mapiranje seta DTO-ova
    Set<RezervacijaDTO> toRezervacijaDTOSet(Set<Rezervacija> rezervacije);
}
