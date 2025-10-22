/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.dusan.villa_sorrento_backend.config;

/**
 *
 * @author Dusan
 */

import com.dusan.villa_sorrento_backend.model.Soba;
import com.dusan.villa_sorrento_backend.model.User;
import com.dusan.villa_sorrento_backend.repository.SobaRepository;
import com.dusan.villa_sorrento_backend.repository.UserRepository;
import java.util.Optional;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner{
    private final UserRepository userRepository;
    private final SobaRepository sobaRepository;
    

    public DataInitializer(UserRepository userRepository, SobaRepository sobaRepository) {
        this.userRepository = userRepository;
        this.sobaRepository = sobaRepository;
        
    }

    @Override
    public void run(String... args) throws Exception {
        //Dodavanje korisnika
        addOrUpdateUser("admin1", "admin1", "admin", "111", "0601111111");
        addOrUpdateUser("admin2", "admin2", "admin", "222", "060222222");
        addOrUpdateUser("klijent1", "klijent1", "klijent", "333", "0603333333");

        //Dodavanje soba 
        addOrUpdateSoba("Dvokrevetna sa pogledom na more", 120.00, "noc", true, "dvokrevetna pm", "dvokrevetna_pm.jpg");
        addOrUpdateSoba("Jednokrevetna bez pogleda na more", 80.00, "noc", true, "jednokrevetna bp", "jednokrevetna_bp.jpg");
        addOrUpdateSoba("Dvokrevetna bez pogleda na more", 100.00, "noc", true, "dvokrevetna bp ", "dvokrevetna_bp.jpeg");
        addOrUpdateSoba("Trokrevetna sa pogledom na more", 150.00, "noc", true, "trokrevetna pm", "trokrevetna_pm.jpg");
        addOrUpdateSoba("Dvokrevetna soba sa pogledom na more", 90.00, "noc", false, "dvokrevetna pm", "dvokrevetna_pm2.jpg");
        addOrUpdateSoba("Dvokrevetna deluxe", 200.00, "noc", true, "dvokrevetna deluxe", "dvokrevetna_deluxe.jpg");

        System.out.println("Inicijalizacija podataka završena.");
    }

    private void addOrUpdateUser(String username, String password, String uloga, String brojIsprave, String brojTelefona) {
        Optional<User> existingUser = userRepository.findByUsername(username);
        if (existingUser.isEmpty()) {
            User newUser = new User();
            newUser.setUsername(username);
            newUser.setPassword(password);
            newUser.setUloga(uloga);
            newUser.setBrojIsprave(brojIsprave);
            newUser.setBrojTelefona(brojTelefona);
            userRepository.save(newUser);
            System.out.println("Korisnik '" + username + "' dodat.");
        } else {
            System.out.println("Korisnik '" + username + "' već postoji.");
            
        }
    }

    private void addOrUpdateSoba(String opis, Double cena, String jedinicaMere, Boolean dostupna, String tipSobe, String slikaUrl) {
       
        if (sobaRepository.findByOpis(opis).isEmpty()) { 
            Soba novaSoba = new Soba();
            novaSoba.setOpis(opis);
            novaSoba.setCena(cena);
            novaSoba.setJedinicaMere(jedinicaMere);
            novaSoba.setDostupna(dostupna);
            novaSoba.setTipSobe(tipSobe);
            novaSoba.setSlikaUrl(slikaUrl); // Postavi putanju do slike
            sobaRepository.save(novaSoba);
            System.out.println("Soba '" + opis + "' dodata.");
        } else {
            System.out.println("Soba '" + opis + "' već postoji.");
        }
    }
}
