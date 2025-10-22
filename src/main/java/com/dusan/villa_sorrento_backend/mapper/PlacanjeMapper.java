/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.dusan.villa_sorrento_backend.mapper;

/**
 *
 * @author Dusan
 */

import com.dusan.villa_sorrento_backend.dto.PlacanjeDTO;
import com.dusan.villa_sorrento_backend.model.Placanje;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface PlacanjeMapper {
    PlacanjeMapper INSTANCE = Mappers.getMapper(PlacanjeMapper.class);

    @Mapping(source = "rezervacija.idRezervacija", target = "rezervacijaId")
    PlacanjeDTO placanjeToPlacanjeDTO(Placanje placanje);

    @Mapping(target = "rezervacija", ignore = true) // Rezervacija se setuje u servisu
    Placanje placanjeDTOToPlacanje(PlacanjeDTO placanjeDTO);
}
