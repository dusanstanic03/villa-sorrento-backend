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
     *
     * @param tipSobeDTO podaci o tipu sobe
     * @return sacuvani tip sobe
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
     *
     * @return lista tipova soba
     */
    public List<TipSobeDTO> getAllTipoviSoba() {
        return tipSobeRepository.findAll().stream()
                .map(tipSobeMapper::tipSobeToTipSobeDTO)
                .collect(Collectors.toList());
    }

    /**
     * Pronalazi tip sobe po identifikatoru.
     *
     * @param id identifikator tipa sobe
     * @return pronadjeni tip sobe
     */
    public TipSobeDTO getTipSobeById(Long id) {
        return tipSobeRepository.findById(id)
                .map(tipSobeMapper::tipSobeToTipSobeDTO)
                .orElseThrow(() -> new EntityNotFoundException("Tip sobe sa ID " + id + " nije pronadjen."));
    }

    /**
     * Azurira postojeci tip sobe.
     *
     * @param id identifikator tipa sobe
     * @param tipSobeDTO novi podaci
     * @return azurirani tip sobe
     */
    public TipSobeDTO updateTipSobe(Long id, TipSobeDTO tipSobeDTO) {
        TipSobe tipSobe = tipSobeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tip sobe sa ID " + id + " nije pronadjen."));
        tipSobeMapper.updateTipSobeFromDto(tipSobeDTO, tipSobe);
        tipSobe.setIdTipSobe(id);
        return tipSobeMapper.tipSobeToTipSobeDTO(tipSobeRepository.save(tipSobe));
    }

    /**
     * Brise tip sobe ako postoji.
     *
     * @param id identifikator tipa sobe
     */
    public void deleteTipSobe(Long id) {
        if (!tipSobeRepository.existsById(id)) {
            throw new EntityNotFoundException("Tip sobe sa ID " + id + " nije pronadjen.");
        }
        tipSobeRepository.deleteById(id);
    }
}
