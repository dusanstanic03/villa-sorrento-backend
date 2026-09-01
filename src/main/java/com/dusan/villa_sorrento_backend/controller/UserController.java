/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.dusan.villa_sorrento_backend.controller;




import com.dusan.villa_sorrento_backend.dto.UserDTO;
import com.dusan.villa_sorrento_backend.dto.UserPrijavaDTO;
import com.dusan.villa_sorrento_backend.dto.UserRegistracijaDTO;
import com.dusan.villa_sorrento_backend.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

/**
 * @author Dusan
 * REST kontroler za upravljanje user-ima.
 */
@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }
    
    // SK2: Registracija klijenta
    @PostMapping("/register")
    public ResponseEntity<UserDTO> registerUser(@RequestBody UserRegistracijaDTO registracijaDTO) {
        try {
            UserDTO newUser = userService.registerUser(registracijaDTO);
            return new ResponseEntity<>(newUser, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT); // 409 Conflict
        }
    }

    // SK1: Prijava klijenta/admina 
    @PostMapping("/login")
    public ResponseEntity<UserDTO> loginUser(@RequestBody UserPrijavaDTO userPrijavaDTO) {
        return userService.loginUser(userPrijavaDTO.getUsername(), userPrijavaDTO.getPassword())
                .map(userDTO -> new ResponseEntity<>(userDTO, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.UNAUTHORIZED)); // 401 Unauthorized
    }

    // SK9: Pretraga klijenata (i admina) - admin ima pristup ovome
    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = userService.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        try {
            UserDTO user = userService.getUserById(id);
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // 404 Not Found
        }
    }

    // SK10: Kreiranje klijenta (admin)
    @PostMapping("/admin/create")
    public ResponseEntity<UserDTO> createUserByAdmin(@RequestBody UserRegistracijaDTO registracijaDTO) {
        try {
            UserDTO newUser = userService.createUserByAdmin(registracijaDTO);
            return new ResponseEntity<>(newUser, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    // SK12: Izmena klijenta (admin)
    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long id, @RequestBody UserDTO userDTO) {
        try {
            UserDTO updatedUser = userService.updateUser(id, userDTO);
            return new ResponseEntity<>(updatedUser, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // Npr. username je već zauzet
        }
    }

    // SK11: Brisanje klijenta (admin)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        try {
            userService.deleteUser(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // 204 No Content
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
     
    
}
