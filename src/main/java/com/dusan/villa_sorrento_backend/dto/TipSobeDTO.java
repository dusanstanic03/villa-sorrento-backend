package com.dusan.villa_sorrento_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO objekat za prenos podataka o tipu sobe kroz REST API.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TipSobeDTO {
    private Long idTipSobe;
    private String naziv;
    private String opis;
    private Integer kapacitet;
}
