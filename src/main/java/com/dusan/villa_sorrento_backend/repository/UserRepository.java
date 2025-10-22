/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.dusan.villa_sorrento_backend.repository;

import com.dusan.villa_sorrento_backend.model.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Dusan
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username); //rezultat treba da bude jedinstven(1 ili nista) pa koristimo Optional umesto List
                                                    //za SK1 i SK2
    boolean existsByUsername(String username); //treba nam za proevru jedinstenosti
    
    
}
