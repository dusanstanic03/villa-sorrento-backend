package com.dusan.villa_sorrento_backend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.dusan.villa_sorrento_backend.dto.UserDTO;
import com.dusan.villa_sorrento_backend.dto.UserRegistracijaDTO;
import com.dusan.villa_sorrento_backend.mapper.UserMapper;
import com.dusan.villa_sorrento_backend.model.User;
import com.dusan.villa_sorrento_backend.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserService userService;

    @Test
    void testLoginUserReturnsUserWhenCredentialsAreValid() {
        User user = new User();
        user.setUsername("pera");
        user.setPassword("tajna");
        UserDTO dto = new UserDTO(1L, "pera", "klijent", "123", "060123");

        when(userRepository.findByUsername("pera")).thenReturn(Optional.of(user));
        when(userMapper.userToUserDTO(user)).thenReturn(dto);

        Optional<UserDTO> result = userService.loginUser("pera", "tajna");

        assertTrue(result.isPresent());
        assertEquals("pera", result.get().getUsername());
    }

    @Test
    void testLoginUserReturnsEmptyWhenPasswordIsInvalid() {
        User user = new User();
        user.setPassword("ispravna");
        when(userRepository.findByUsername("pera")).thenReturn(Optional.of(user));

        Optional<UserDTO> result = userService.loginUser("pera", "pogresna");

        assertTrue(result.isEmpty());
        verify(userMapper, never()).userToUserDTO(any(User.class));
    }

    @Test
    void testRegisterUserCreatesClientRole() {
        UserRegistracijaDTO registracijaDTO = new UserRegistracijaDTO("novi", "pass", "123", "060123");
        User mappedUser = new User();
        User savedUser = new User();
        savedUser.setIdUser(5L);
        savedUser.setUsername("novi");
        savedUser.setUloga("klijent");
        UserDTO savedDto = new UserDTO(5L, "novi", "klijent", "123", "060123");

        when(userRepository.existsByUsername("novi")).thenReturn(false);
        when(userMapper.userRegistracijaDTOToUser(registracijaDTO)).thenReturn(mappedUser);
        when(userRepository.save(mappedUser)).thenReturn(savedUser);
        when(userMapper.userToUserDTO(savedUser)).thenReturn(savedDto);

        UserDTO result = userService.registerUser(registracijaDTO);

        assertEquals("klijent", mappedUser.getUloga());
        assertEquals("pass", mappedUser.getPassword());
        assertEquals(savedDto, result);
    }

    @Test
    void testRegisterUserThrowsWhenUsernameExists() {
        UserRegistracijaDTO registracijaDTO = new UserRegistracijaDTO("postoji", "pass", "123", "060123");
        when(userRepository.existsByUsername("postoji")).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> userService.registerUser(registracijaDTO));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testGetUserByIdThrowsWhenUserDoesNotExist() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> userService.getUserById(99L));
    }

    @Test
    void testDeleteUserDeletesExistingUser() {
        when(userRepository.existsById(1L)).thenReturn(true);

        userService.deleteUser(1L);

        verify(userRepository).deleteById(1L);
    }
}
