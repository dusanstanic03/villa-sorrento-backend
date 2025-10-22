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

public class SobaDTO {

    private Long idSoba;
    private String opis;
    private Double cena;
    private String jedinicaMere;
    private Boolean dostupna;
    private String tipSobe;
    private String slikaUrl;
}
