/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.dusan.villa_sorrento_backend.mapper;

import com.dusan.villa_sorrento_backend.dto.StavkaRezervacijeDTO;
import com.dusan.villa_sorrento_backend.model.Gost;
import com.dusan.villa_sorrento_backend.model.StavkaRezervacije;
import com.dusan.villa_sorrento_backend.model.Usluga;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 *
 * @author Dusan
 */
@Mapper(componentModel = "spring", uses = {GostMapper.class, UslugaMapper.class})
public interface StavkaRezervacijeMapper {

    @Mapping(source = "soba.idSoba", target = "sobaId") //entitet ima Soba soba a DTP Long sobaId, pa nam treba samo idSoba 
    @Mapping(target = "gostIds", expression = "java(mapGostIds(stavkaRezervacije.getGosti()))")
    @Mapping(target = "uslugaIds", expression = "java(mapUslugaIds(stavkaRezervacije.getUsluge()))")
    StavkaRezervacijeDTO stavkaRezervacijeToStavkaRezervacijeDTO(StavkaRezervacije stavkaRezervacije);

    @Mapping(target = "rezervacija", ignore = true) // Rezervacija će se setovati u servisu
    @Mapping(target = "soba", ignore = true) // Soba će se setovati u servisu
    @Mapping(target = "gosti", ignore = true)
    @Mapping(target = "usluge", ignore = true)
    StavkaRezervacije stavkaRezervacijeDTOToStavkaRezervacije(StavkaRezervacijeDTO stavkaRezervacijeDTO);

    default Set<Long> mapGostIds(Set<Gost> gosti) {
        if (gosti == null) {
            return Set.of();
        }
        return gosti.stream().map(Gost::getIdGost).collect(Collectors.toSet());
    }

    default Set<Long> mapUslugaIds(Set<Usluga> usluge) {
        if (usluge == null) {
            return Set.of();
        }
        return usluge.stream().map(Usluga::getIdUsluga).collect(Collectors.toSet());
    }
}
