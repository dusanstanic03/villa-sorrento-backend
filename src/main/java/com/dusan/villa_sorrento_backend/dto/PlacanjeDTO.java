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

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class PlacanjeDTO {
    private Long idPlacanje;
    private String status;
    private String nacinPlacanja;
    private Double iznos;
    private LocalDate datumPlacanja;
    private Long rezervacijaId;
}
