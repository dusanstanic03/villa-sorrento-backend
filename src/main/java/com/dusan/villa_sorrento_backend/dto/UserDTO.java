/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.dusan.villa_sorrento_backend.dto;

/**
 *
 * @author Dusan
 */

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class UserDTO {
    private Long idUser;
    private String username;
    private String uloga;
    private String brojIsprave;
    private String brojTelefona;
    // Password necemo slati u DTO-u nakon registracije/prijave
}

