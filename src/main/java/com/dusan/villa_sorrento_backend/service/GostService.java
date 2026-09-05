package com.dusan.villa_sorrento_backend.service;

import com.dusan.villa_sorrento_backend.dto.GostDTO;
import com.dusan.villa_sorrento_backend.mapper.GostMapper;
import com.dusan.villa_sorrento_backend.model.Gost;
import com.dusan.villa_sorrento_backend.repository.GostRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

/**
 * Servis za sistemske operacije sa gostima.
 * Klasa sadrzi logiku za kreiranje, pregled, izmenu i brisanje gostiju.
 * Gost predstavlja osobu koja boravi u sobi u okviru stavke rezervacije i ne
 * mora biti korisnik sistema.
 *
 * @author Dusan
 */
@Service
public class GostService {
    private final GostRepository gostRepository;
    private final GostMapper gostMapper;

    public GostService(GostRepository gostRepository, GostMapper gostMapper) {
        this.gostRepository = gostRepository;
        this.gostMapper = gostMapper;
    }

    /**
     * Kreira novog gosta ili vraca postojeceg gosta sa istim brojem isprave.
     * Metoda prvo proverava da li u bazi vec postoji gost sa prosledjenim brojem
     * isprave. Ako postoji, postojeci gost se mapira u DTO i vraca kao rezultat.
     * Ako ne postoji, kreira se novi gost, cuva u bazi i vraca kao DTO.
     *
     * @param gostDTO podaci o gostu koji se kreira
     * @return postojeci ili novokreirani gost predstavljen kao DTO
     */
    public GostDTO createGost(GostDTO gostDTO) {
        return gostRepository.findByBrojIsprave(gostDTO.getBrojIsprave())
                .map(gostMapper::gostToGostDTO)
                .orElseGet(() -> saveNewGost(gostDTO));
    }

    /**
     * Cuva novog gosta u bazi.
     * Metoda mapira DTO u domenski objekat gosta, cuva ga u bazi i vraca DTO
     * sacuvanog gosta.
     *
     * @param gostDTO podaci o gostu koji se cuva
     * @return sacuvani gost predstavljen kao DTO
     */
    private GostDTO saveNewGost(GostDTO gostDTO) {
        Gost saved = gostRepository.save(gostMapper.gostDTOToGost(gostDTO));
        return gostMapper.gostToGostDTO(saved);
    }

    /**
     * Vraca sve goste iz sistema.
     * Metoda ucitava sve goste iz baze, mapira ih u DTO objekte i vraca listu.
     *
     * @return lista svih gostiju predstavljenih kao DTO objekti
     */
    public List<GostDTO> getAllGosti() {
        return gostRepository.findAll().stream()
                .map(gostMapper::gostToGostDTO)
                .collect(Collectors.toList());
    }

    /**
     * Pronalazi gosta po identifikatoru.
     * Metoda pretrazuje bazu po identifikatoru gosta. Ako gost postoji, vraca se
     * njegov DTO prikaz. Ako ne postoji, baca se izuzetak.
     *
     * @param id identifikator gosta koji se pretrazuje
     * @return pronadjeni gost predstavljen kao DTO
     * @throws EntityNotFoundException ako gost sa zadatim identifikatorom ne postoji
     */
    public GostDTO getGostById(Long id) {
        return gostRepository.findById(id)
                .map(gostMapper::gostToGostDTO)
                .orElseThrow(() -> new EntityNotFoundException("Gost sa ID " + id + " nije pronadjen."));
    }

    /**
     * Azurira podatke postojeceg gosta.
     * Metoda prvo pronalazi gosta po identifikatoru. Ako gost postoji, vrednosti iz
     * DTO objekta se prenose na postojeci domenski objekat i izmenjeni gost se cuva
     * u bazi.
     *
     * @param id identifikator gosta koji se azurira
     * @param gostDTO novi podaci o gostu
     * @return azurirani gost predstavljen kao DTO
     * @throws EntityNotFoundException ako gost sa zadatim identifikatorom ne postoji
     */
    public GostDTO updateGost(Long id, GostDTO gostDTO) {
        Gost gost = gostRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Gost sa ID " + id + " nije pronadjen."));
        gostMapper.updateGostFromDto(gostDTO, gost);
        return gostMapper.gostToGostDTO(gostRepository.save(gost));
    }

    /**
     * Brise gosta po identifikatoru.
     * Metoda proverava da li gost postoji u bazi. Ako postoji, gost se brise.
     * Ako ne postoji, baca se izuzetak.
     *
     * @param id identifikator gosta koji se brise
     * @throws EntityNotFoundException ako gost sa zadatim identifikatorom ne postoji
     */
    public void deleteGost(Long id) {
        if (!gostRepository.existsById(id)) {
            throw new EntityNotFoundException("Gost sa ID " + id + " nije pronadjen.");
        }
        gostRepository.deleteById(id);
    }
}
