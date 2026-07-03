package com.dusan.villa_sorrento_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO objekat za prenos podataka o usluzi.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UslugaDTO {
    private Long idUsluga;
    private String naziv;
    private String opis;
    private Double cena;
    private Boolean aktivna;
}
