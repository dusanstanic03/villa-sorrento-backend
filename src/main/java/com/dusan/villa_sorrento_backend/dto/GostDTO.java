package com.dusan.villa_sorrento_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO objekat za prenos podataka o gostu.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GostDTO {
    private Long idGost;
    private String ime;
    private String prezime;
    private String brojIsprave;
    private String brojTelefona;
}
