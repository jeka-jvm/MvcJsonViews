package org.example.mvcjsonview.service;

import org.example.mvcjsonview.entity.User;
import org.example.mvcjsonview.exception.UserAlreadyExistsException;
import org.example.mvcjsonview.exception.UserNotFoundException;
import org.example.mvcjsonview.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@SpringBootTest
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void createUser_Success() {
        User user = new User(null, "Олег", "oleg@mail.ru", null);
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());
        when(userRepository.save(user)).thenReturn(user);

        User createdUser = userService.createUser(user);

        assertEquals("Олег", createdUser.getName());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void createUser_UserAlreadyExists() {
        User user = new User(null, "Олег", "oleg@mail.ru", null);
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        assertThrows(UserAlreadyExistsException.class, () -> userService.createUser(user));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void getUserById_UserFound() {
        User user = new User(1L, "Олег", "oleg@mail.ru", null);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        Optional<User> foundUser = userService.getUserById(1L);

        assertTrue(foundUser.isPresent());
        assertEquals("Олег", foundUser.get().getName());
    }

    @Test
    void getUserById_UserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<User> foundUser = userService.getUserById(1L);

        assertFalse(foundUser.isPresent());
    }

    @Test
    void updateUser_UserExists() {
        User existingUser = new User(1L, "Олег", "oleg@mail.ru", null);
        User updatedDetails = new User(null, "Олег", "oleg@mail.ru", null);
        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(existingUser)).thenReturn(existingUser);

        User updatedUser = userService.updateUser(1L, updatedDetails);

        assertEquals("Олег", updatedUser.getName());
        assertEquals("oleg@mail.ru", updatedUser.getEmail());
        verify(userRepository, times(1)).save(existingUser);
    }

    @Test
    void updateUser_UserNotFound() {
        User updatedDetails = new User(null, "Олег", "oleg@mail.ru", null);
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.updateUser(1L, updatedDetails));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void deleteUser_UserExists() {
        when(userRepository.existsById(1L)).thenReturn(true);

        userService.deleteUser(1L);

        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteUser_UserNotFound() {
        when(userRepository.existsById(1L)).thenReturn(false);

        assertThrows(UserNotFoundException.class, () -> userService.deleteUser(1L));
        verify(userRepository, never()).deleteById(1L);
    }
}