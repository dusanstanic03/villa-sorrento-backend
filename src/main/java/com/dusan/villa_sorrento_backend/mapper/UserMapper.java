/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.dusan.villa_sorrento_backend.mapper;

/**
 *
 * @author Dusan
 */

import com.dusan.villa_sorrento_backend.dto.UserDTO;
import com.dusan.villa_sorrento_backend.dto.UserRegistracijaDTO;
import com.dusan.villa_sorrento_backend.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;


@Mapper(componentModel = "spring") //Ovo treba da omoguci da Spring inject-uje mapper-e
public interface UserMapper {
    
    UserMapper INSTANCE =  Mappers.getMapper(UserMapper.class);
    
    UserDTO userToUserDTO(User user);
    
    @Mapping(target = "idUser", ignore = true) //ignorise se idUser pri registraciji jer treba da ga dodeli baza
    @Mapping(target = "rezervacije", ignore = true) //ignosrisu se rezervacije pri kreiranju
    @Mapping(target = "uloga", ignore = true)
    User userRegistracijaDTOToUser(UserRegistracijaDTO userRegistracijaDTO);
    
    //Mapiranje za izmenu user-a
    @Mapping(target = "password", ignore = true) // Lozinka se ne menja direktno ovim mapperom
    @Mapping(target = "rezervacije", ignore = true)
    void updateUserFromDto(UserDTO userDTO, @MappingTarget User user);
    
}
