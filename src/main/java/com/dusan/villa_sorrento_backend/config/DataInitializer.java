/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.dusan.villa_sorrento_backend.config;

import com.dusan.villa_sorrento_backend.model.Soba;
import com.dusan.villa_sorrento_backend.model.TipSobe;
import com.dusan.villa_sorrento_backend.model.Usluga;
import com.dusan.villa_sorrento_backend.model.User;
import com.dusan.villa_sorrento_backend.repository.SobaRepository;
import com.dusan.villa_sorrento_backend.repository.TipSobeRepository;
import com.dusan.villa_sorrento_backend.repository.UslugaRepository;
import com.dusan.villa_sorrento_backend.repository.UserRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 *
 * @author Dusan
 */
@Component
public class DataInitializer implements CommandLineRunner{
    private final UserRepository userRepository;
    private final SobaRepository sobaRepository;
    private final TipSobeRepository tipSobeRepository;
    private final UslugaRepository uslugaRepository;

    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);



    public DataInitializer(UserRepository userRepository, SobaRepository sobaRepository, TipSobeRepository tipSobeRepository, UslugaRepository uslugaRepository) {
        this.userRepository = userRepository;
        this.sobaRepository = sobaRepository;
        this.tipSobeRepository = tipSobeRepository;
        this.uslugaRepository = uslugaRepository;

    }

    @Override
    public void run(String... args) {
        //Dodavanje korisnika
        addOrUpdateUser("admin1", "admin1", "admin", "111", "0601111111");
        addOrUpdateUser("admin2", "admin2", "admin", "222", "060222222");
        addOrUpdateUser("klijent1", "klijent1", "klijent", "333", "0603333333");

        addOrUpdateTipSobe("jednokrevetna bp", "Jednokrevetna soba bez pogleda na more", 1);
        addOrUpdateTipSobe("dvokrevetna bp", "Dvokrevetna soba bez pogleda na more", 2);
        addOrUpdateTipSobe("dvokrevetna pm", "Dvokrevetna soba sa pogledom na more", 2);
        addOrUpdateTipSobe("trokrevetna pm", "Trokrevetna soba sa pogledom na more", 3);
        addOrUpdateTipSobe("dvokrevetna deluxe", "Deluxe dvokrevetna soba", 2);

        addOrUpdateUsluga("Dorucak", "Dorucak za jednog gosta", 12.00, true);
        addOrUpdateUsluga("Parking", "Parking mesto za jedan dan", 8.00, true);
        addOrUpdateUsluga("Spa paket", "Koriscenje spa centra", 30.00, true);

        //Dodavanje soba 
        addOrUpdateSoba("Dvokrevetna sa pogledom na more", 120.00, "noc", true, "dvokrevetna pm", "dvokrevetna_pm.jpg");
        addOrUpdateSoba("Jednokrevetna bez pogleda na more", 80.00, "noc", true, "jednokrevetna bp", "jednokrevetna_bp.jpg");
        addOrUpdateSoba("Dvokrevetna bez pogleda na more", 100.00, "noc", true, "dvokrevetna bp", "dvokrevetna_bp.jpeg");
        addOrUpdateSoba("Trokrevetna sa pogledom na more", 150.00, "noc", true, "trokrevetna pm", "trokrevetna_pm.jpg");
        addOrUpdateSoba("Dvokrevetna soba sa pogledom na more", 90.00, "noc", false, "dvokrevetna pm", "dvokrevetna_pm2.jpg");
        addOrUpdateSoba("Dvokrevetna deluxe", 200.00, "noc", true, "dvokrevetna deluxe", "dvokrevetna_deluxe.jpg");

        logger.info("Inicijalizacija podataka završena.");
    }

    private void addOrUpdateUser(String username, String password, String uloga, String brojIsprave, String brojTelefona) {
        if (userRepository.findByUsername(username).isEmpty()) {
            User newUser = new User();
            newUser.setUsername(username);
            newUser.setPassword(password);
            newUser.setUloga(uloga);
            newUser.setBrojIsprave(brojIsprave);
            newUser.setBrojTelefona(brojTelefona);
            userRepository.save(newUser);
            logger.info("Korisnik '{}' dodat.", newUser);
        }
    }

    private void addOrUpdateSoba(String opis, Double cena, String jedinicaMere, Boolean dostupna, String tipSobe, String slikaUrl) {

        if (sobaRepository.findByOpis(opis).isEmpty()) { 
            TipSobe tip = tipSobeRepository.findByNaziv(tipSobe)
                    .orElseThrow(() -> new IllegalStateException("Tip sobe " + tipSobe + " nije inicijalizovan."));
            Soba novaSoba = new Soba();
            novaSoba.setOpis(opis);
            novaSoba.setCena(cena);
            novaSoba.setJedinicaMere(jedinicaMere);
            novaSoba.setDostupna(dostupna);
            novaSoba.setTipSobe(tip);
            novaSoba.setSlikaUrl(slikaUrl);
            sobaRepository.save(novaSoba);
            logger.info("Soba '{}' dodat.", novaSoba);
        }
    }

    private void addOrUpdateTipSobe(String naziv, String opis, Integer kapacitet) {
        if (tipSobeRepository.findByNaziv(naziv).isEmpty()) {
            TipSobe tipSobe = new TipSobe();
            tipSobe.setNaziv(naziv);
            tipSobe.setOpis(opis);
            tipSobe.setKapacitet(kapacitet);
            tipSobeRepository.save(tipSobe);
            logger.info("Tip sobe '{}' dodat.", tipSobe);
        }
    }

    private void addOrUpdateUsluga(String naziv, String opis, Double cena, Boolean aktivna) {
        if (uslugaRepository.findByNaziv(naziv).isEmpty()) {
            Usluga usluga = new Usluga();
            usluga.setNaziv(naziv);
            usluga.setOpis(opis);
            usluga.setCena(cena);
            usluga.setAktivna(aktivna);
            uslugaRepository.save(usluga);
            logger.info("Usluga '{}' dodata.", usluga);
        }
    }
}
