/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.dusan.villa_sorrento_backend.service;

import com.dusan.villa_sorrento_backend.dto.UserDTO;
import com.dusan.villa_sorrento_backend.dto.UserRegistracijaDTO;
import com.dusan.villa_sorrento_backend.mapper.UserMapper;
import com.dusan.villa_sorrento_backend.model.User;
import com.dusan.villa_sorrento_backend.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

/**
 * Servis za sistemske operacije sa klijentima.
 * Obuhvata prijavu, registraciju klijenta, kreiranje od strane admina,
 * pretragu, izmenu i brisanje klijenata.
 * Servis koristi repozitorijum za pristup bazi i mapper za konverziju izmedju
 * domenske klase {@link com.dusan.villa_sorrento_backend.model.User} i DTO objekata.
 *
 * @author Dusan
 */
@Service
public class UserService {
    
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    
    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    /**
     * Prijavljuje kljenta na osnovu korisnickog imena i lozinke.
     * Metoda prvo pronalazi korisnika po korisnickom imenu. Ako korisnik postoji,
     * proverava da li prosledjena lozinka odgovara lozinki sacuvanoj u bazi.
     * Ako su podaci ispravni, korisnik se mapira u DTO i vraca kao rezultat unutar Optional-a.
     * Ako korisnik ne postoji ili lozinka nije ispravna, vraca se prazan Optional.
     * @param username korisnicko ime klijenta koji pokusava prijavu
     * @param password lozinka klijenta koji pokusava prijavu
     * @return Optional sa korisnikom ako su kredencijali ispravni, u suprotnom prazan Optional
     */
    public Optional<UserDTO> loginUser(String username, String password){
        Optional<User> userOptional = userRepository.findByUsername(username);
        if(userOptional.isPresent()){
            User user = userOptional.get();
            if(password.equals(user.getPassword())){
                return Optional.of(userMapper.userToUserDTO(user));
            }
        }
        return Optional.empty();
    }
    
    /**
     * Registruje novog klijenta u sistemu.
     * Metoda proverava da li je korisnicko ime vec zauzeto. Ako korisnicko ime
     * nije zauzeto, podaci iz registracionog DTO objekta se mapiraju u domenski
     * objekat, postavlja se lozinka i korisniku se dodeljuje uloga klijent.
     * Nakon cuvanja u bazi, sacuvani korisnik se vraca kao DTO.
     *
     * @param registracijaDTO podaci potrebni za registraciju korisnika
     * @return registrovani korisnik tipa UserDTO
     * @throws IllegalArgumentException ako korisnicko ime vec postoji u bazi
     */
    public UserDTO registerUser(UserRegistracijaDTO registracijaDTO){
        if(userRepository.existsByUsername(registracijaDTO.getUsername())){
            throw new IllegalArgumentException("Korisnicko ime je vec zauzeto.");
        }
        User newUser = userMapper.userRegistracijaDTOToUser(registracijaDTO);
        newUser.setPassword(registracijaDTO.getPassword());
        newUser.setUloga("klijent");
        User savedUser = userRepository.save(newUser);
        return userMapper.userToUserDTO(savedUser);
    }

    /**
     * Vraca sve korisnike sistema.
     * Metoda ucitava sve korisnike iz baze, mapira svaki domenski objekat u DTO
     * i vraca listu DTO objekata. Koristi se za pregled korisnika od strane admina.
     *
     * @return lista svih korisnika u formi DTO objekata
     */
    public List<UserDTO> getAllUsers(){
        List<User> users = userRepository.findAll();
        List<UserDTO> userDTOs = new ArrayList<>();
        for (User user : users) {
            UserDTO userDTO = userMapper.userToUserDTO(user);
            userDTOs.add(userDTO);
        }
        return userDTOs;
    }

    /**
     * Pronalazi korisnika po identifikatoru.
     * Metoda pokusava da pronadje korisnika sa zadatim identifikatorom. Ako korisnik
     * postoji, mapira se u DTO i vraca kao rezultat. Ako korisnik ne postoji, baca
     * se izuzetak.
     *
     * @param id identifikator korisnika koji se pretrazuje
     * @return pronadjeni korisnik predstavljen u formi DTO-a
     * @throws EntityNotFoundException ako korisnik sa zadatim identifikatorom ne postoji
     */
    public UserDTO getUserById(Long id) {
       
        Optional<User> optionalUser = userRepository.findById(id);

        if (optionalUser.isEmpty()) { 
            throw new EntityNotFoundException("Klijent sa ID " + id + " nije pronađen.");
        }
        User user = optionalUser.get();
        return userMapper.userToUserDTO(user);
    }

    /**
     * Kreiranje korisnika od strane admina.
     * Metoda koristi istu poslovnu logiku kao registracija klijenta. Administrator
     * prosledjuje podatke za kreiranje korisnika, a metoda kreira novog korisnika
     * sa ulogom klijent.
     *
     * @param registracijaDTO podaci potrebni za kreiranje korisnika
     * @return kreirani korisnik u formi DTO-a
     * @throws IllegalArgumentException ako korisnicko ime vec postoji u bazi
     */
    public UserDTO createUserByAdmin(UserRegistracijaDTO registracijaDTO){
        return registerUser(registracijaDTO);
    }

    /**
     * Azurira podatke postojeceg korisnika.
     * Metoda prvo pronalazi korisnika po identifikatoru. Ako korisnik postoji,
     * azuriraju se dozvoljena polja: korisnicko ime, broj isprave, broj telefona
     * i uloga. Lozinka se ovom metodom ne menja. Nakon cuvanja izmenjenog objekta
     * u bazi, azurirani korisnik se vraca kao DTO.
     *
     * @param id identifikator korisnika koji se azurira
     * @param userDTO novi podaci o korisniku
     * @return azurirani korisnik u formi DTO-a
     * @throws EntityNotFoundException ako korisnik sa zadatim identifikatorom ne postoji
     */
    public UserDTO updateUser(Long id, UserDTO userDTO){
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Klijent sa ID " + id + " nije pronađen."));
        
        //azuriramo samo polja koja su dozvoljena za izmenu
        existingUser.setUsername(userDTO.getUsername());
        existingUser.setBrojIsprave(userDTO.getBrojIsprave());
        existingUser.setBrojTelefona(userDTO.getBrojTelefona());
        existingUser.setUloga(userDTO.getUloga());
        
        User updatedUser = userRepository.save(existingUser);
        return userMapper.userToUserDTO(updatedUser);
    }

    /**
     * Brise korisnika po identifikatoru.
     * Metoda proverava da li korisnik sa zadatim identifikatorom postoji. Ako postoji,
     * korisnik se brise iz baze. Ako ne postoji, baca se izuzetak.
     *
     * @param id identifikator korisnika koji se brise
     * @throws EntityNotFoundException ako korisnik sa zadatim identifikatorom ne postoji
     */
    public void deleteUser(Long id){
        if(!userRepository.existsById(id)){
            throw new EntityNotFoundException("Klijent sa ID " + id + " nije pronađen.");
        }
        userRepository.deleteById(id);
    }
}
