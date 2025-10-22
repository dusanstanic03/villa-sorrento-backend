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
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class RezervacijaDTO {
    private Long idRezervacija;
    private Double iznos;
    private LocalDate datumKreiranja;
    private Long userId;
    private Set<StavkaRezervacijeDTO> stavkeRezervacije;
}
