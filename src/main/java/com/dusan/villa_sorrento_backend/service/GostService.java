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
 * Servis za sistemske operacije nad gostima.
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
     * Kreira novog gosta.
     *
     * @param gostDTO podaci o gostu
     * @return sacuvani gost
     */
    public GostDTO createGost(GostDTO gostDTO) {
        return gostRepository.findByBrojIsprave(gostDTO.getBrojIsprave())
                .map(gostMapper::gostToGostDTO)
                .orElseGet(() -> saveNewGost(gostDTO));
    }

    private GostDTO saveNewGost(GostDTO gostDTO) {
        Gost saved = gostRepository.save(gostMapper.gostDTOToGost(gostDTO));
        return gostMapper.gostToGostDTO(saved);
    }

    /**
     * Vraca sve goste.
     *
     * @return lista gostiju
     */
    public List<GostDTO> getAllGosti() {
        return gostRepository.findAll().stream()
                .map(gostMapper::gostToGostDTO)
                .collect(Collectors.toList());
    }

    /**
     * Pronalazi gosta po identifikatoru.
     *
     * @param id identifikator gosta
     * @return pronadjeni gost
     */
    public GostDTO getGostById(Long id) {
        return gostRepository.findById(id)
                .map(gostMapper::gostToGostDTO)
                .orElseThrow(() -> new EntityNotFoundException("Gost sa ID " + id + " nije pronadjen."));
    }

    /**
     * Azurira podatke o gostu.
     *
     * @param id identifikator gosta
     * @param gostDTO novi podaci
     * @return azurirani gost
     */
    public GostDTO updateGost(Long id, GostDTO gostDTO) {
        Gost gost = gostRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Gost sa ID " + id + " nije pronadjen."));
        gostMapper.updateGostFromDto(gostDTO, gost);
        return gostMapper.gostToGostDTO(gostRepository.save(gost));
    }

    /**
     * Brise gosta ako postoji.
     *
     * @param id identifikator gosta
     */
    public void deleteGost(Long id) {
        if (!gostRepository.existsById(id)) {
            throw new EntityNotFoundException("Gost sa ID " + id + " nije pronadjen.");
        }
        gostRepository.deleteById(id);
    }
}
