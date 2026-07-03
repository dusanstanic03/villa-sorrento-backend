/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.dusan.villa_sorrento_backend.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;
import java.util.Set;

/**
 * DTO objekat za stavku rezervacije.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StavkaRezervacijeDTO {
    private Long id;
    private int rb;
    private LocalDate datumOd;
    private LocalDate datumDo;
    private Double iznos;
    private Long sobaId;
    private Set<Long> gostIds;
    private Set<Long> uslugaIds;
    private Set<GostDTO> gosti;
    private Set<UslugaDTO> usluge;
}
