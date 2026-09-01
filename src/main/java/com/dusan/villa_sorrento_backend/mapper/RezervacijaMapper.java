/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.dusan.villa_sorrento_backend.mapper;

import com.dusan.villa_sorrento_backend.dto.RezervacijaDTO;
import com.dusan.villa_sorrento_backend.model.Rezervacija;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Set;

/**
 *
 * @author Dusan
 */
@Mapper(componentModel = "spring", uses = {StavkaRezervacijeMapper.class}) // Koristi se StavkaRezervacijeMapper zbog seta dole (i entitet i DTO imaju setStav)
public interface RezervacijaMapper {
    
    @Mapping(source = "user.idUser", target = "userId")
    RezervacijaDTO rezervacijaToRezervacijaDTO(Rezervacija rezervacija);

    @Mapping(target = "user", ignore = true) // User se setuje u servisu
    @Mapping(target = "placanja", ignore = true) // Placanjima se upravljaja posebno
    @Mapping(target = "stavkeRezervacije", ignore = true)
    Rezervacija rezervacijaDTOToRezervacija(RezervacijaDTO rezervacijaDTO);

    // Za mapiranje seta DTO-ova
    Set<RezervacijaDTO> toRezervacijaDTOSet(Set<Rezervacija> rezervacije);
}
