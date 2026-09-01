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
 */
@Service
public class UserService {
    
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    //@Autowired dodati Spring Seecurity za password
    
    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }
    
    
    
    /**
     * Prijavljuje kljenta na osnovu korisnickog imena i lozinke.
     *
     * @param username korisnicko ime
     * @param password lozinka
     * @return prijavljeni klijent ako su kredencijali ispravni
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
     * Registruje novog klijenta.
     *
     * @param registracijaDTO podaci za registraciju
     * @return registrovani klijent
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
     * Vraca sve klijente.
     *
     * @return lista klijenata
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
     * Pronalazi klijenta po identifikatoru.
     *
     * @param id identifikator klijenta
     * @return pronadjeni klijent
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
     * Kreira klijenta kroz admin deo aplikacije.
     *
     * @param registracijaDTO podaci za kreiranje klijenta
     * @return kreirani klijent
     */
    public UserDTO createUserByAdmin(UserRegistracijaDTO registracijaDTO){
        return registerUser(registracijaDTO);
    }
    
    /**
     * Azurira podatke postojeceg klijenta.
     *
     * @param id identifikator klijenta
     * @param userDTO novi podaci o klijentu
     * @return azurirani klijent
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
     * Brise klijenta po identifikatoru.
     *
     * @param id identifikator klijenta
     */
    public void deleteUser(Long id){
        if(!userRepository.existsById(id)){
            throw new EntityNotFoundException("Klijent sa ID " + id + " nije pronađen.");
        }
        userRepository.deleteById(id);
    }
}
