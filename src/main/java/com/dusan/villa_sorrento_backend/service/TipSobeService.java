package com.dusan.villa_sorrento_backend.service;

import com.dusan.villa_sorrento_backend.dto.TipSobeDTO;
import com.dusan.villa_sorrento_backend.mapper.TipSobeMapper;
import com.dusan.villa_sorrento_backend.model.TipSobe;
import com.dusan.villa_sorrento_backend.repository.TipSobeRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

/**
 * Servis za sistemske operacije sa tipovima soba.
 * Klasa sadrzi logiku za kreiranje, pregled, izmenu i brisanje tipova
 * soba.
 *
 * @author Dusan
 */
@Service
public class TipSobeService {
    private final TipSobeRepository tipSobeRepository;
    private final TipSobeMapper tipSobeMapper;

    public TipSobeService(TipSobeRepository tipSobeRepository, TipSobeMapper tipSobeMapper) {
        this.tipSobeRepository = tipSobeRepository;
        this.tipSobeMapper = tipSobeMapper;
    }

    /**
     * Kreira novi tip sobe.
     * Metoda proverava da li vec postoji tip sobe sa istim nazivom. Ako ne postoji,
     * DTO se mapira u domenski objekat i cuva u bazi. Sacuvani tip sobe se zatim
     * mapira u DTO i vraca kao rezultat.
     *
     * @param tipSobeDTO podaci o tipu sobe koji se kreira
     * @return sacuvani tip sobe predstavljen kao DTO
     * @throws IllegalArgumentException ako tip sobe sa istim nazivom vec postoji
     */
    public TipSobeDTO createTipSobe(TipSobeDTO tipSobeDTO) {
        if (tipSobeRepository.findByNaziv(tipSobeDTO.getNaziv()).isPresent()) {
            throw new IllegalArgumentException("Tip sobe sa nazivom " + tipSobeDTO.getNaziv() + " vec postoji.");
        }
        TipSobe saved = tipSobeRepository.save(tipSobeMapper.tipSobeDTOToTipSobe(tipSobeDTO));
        return tipSobeMapper.tipSobeToTipSobeDTO(saved);
    }

    /**
     * Vraca sve tipove soba.
     * Metoda ucitava sve tipove soba iz baze, mapira ih u DTO objekte i vraca listu.
     *
     * @return lista svih tipova soba predstavljenih kao DTO objekti
     */
    public List<TipSobeDTO> getAllTipoviSoba() {
        return tipSobeRepository.findAll().stream()
                .map(tipSobeMapper::tipSobeToTipSobeDTO)
                .collect(Collectors.toList());
    }

    /**
     * Pronalazi tip sobe po identifikatoru.
     * Metoda pretrazuje bazu po identifikatoru tipa sobe. Ako tip sobe postoji,
     * vraca se njegov DTO prikaz. Ako ne postoji, baca se izuzetak.
     *
     * @param id identifikator tipa sobe koji se pretrazuje
     * @return pronadjeni tip sobe predstavljen kao DTO
     * @throws EntityNotFoundException ako tip sobe sa zadatim identifikatorom ne postoji
     */
    public TipSobeDTO getTipSobeById(Long id) {
        return tipSobeRepository.findById(id)
                .map(tipSobeMapper::tipSobeToTipSobeDTO)
                .orElseThrow(() -> new EntityNotFoundException("Tip sobe sa ID " + id + " nije pronadjen."));
    }

    /**
     * Azurira postojeci tip sobe.
     * Metoda prvo pronalazi tip sobe po identifikatoru. Ako postoji, vrednosti iz
     * DTO objekta se prenose na postojeci domenski objekat, zadrzava se isti
     * identifikator i izmenjeni tip sobe se cuva u bazi.
     *
     * @param id identifikator tipa sobe koji se azurira
     * @param tipSobeDTO novi podaci o tipu sobe
     * @return azurirani tip sobe predstavljen kao DTO
     * @throws EntityNotFoundException ako tip sobe sa zadatim identifikatorom ne postoji
     */
    public TipSobeDTO updateTipSobe(Long id, TipSobeDTO tipSobeDTO) {
        TipSobe tipSobe = tipSobeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tip sobe sa ID " + id + " nije pronadjen."));
        tipSobeMapper.updateTipSobeFromDto(tipSobeDTO, tipSobe);
        tipSobe.setIdTipSobe(id);
        return tipSobeMapper.tipSobeToTipSobeDTO(tipSobeRepository.save(tipSobe));
    }

    /**
     * Brise tip sobe po identifikatoru.
     * Metoda proverava da li tip sobe postoji u bazi. Ako postoji, brise se iz baze.
     * Ako ne postoji, baca se izuzetak.
     *
     * @param id identifikator tipa sobe koji se brise
     * @throws EntityNotFoundException ako tip sobe sa zadatim identifikatorom ne postoji
     */
    public void deleteTipSobe(Long id) {
        if (!tipSobeRepository.existsById(id)) {
            throw new EntityNotFoundException("Tip sobe sa ID " + id + " nije pronadjen.");
        }
        tipSobeRepository.deleteById(id);
    }
}
