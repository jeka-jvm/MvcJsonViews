package org.example.mvcjsonview.controller;

import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import org.example.mvcjsonview.entity.Order;
import org.example.mvcjsonview.entity.User;
import org.example.mvcjsonview.service.UserService;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;


@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    void createUser_Success() throws Exception {
        User user = new User(null, "Олег", "oleg@mail.ru", null);
        User savedUser = new User(1L, "Олег", "oleg@mail.ru", null);

        when(userService.createUser(any(User.class))).thenReturn(savedUser);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Олег\", \"email\":\"oleg@mail.ru\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Олег"))
                .andExpect(jsonPath("$.email").value("oleg@mail.ru"));
    }

    @Test
    void getAllUsers_Success() throws Exception {
        List<User> users = Arrays.asList(
                new User(1L, "Олег", "oleg@mail.ru", null),
                new User(2L, "Олег", "oleg@mail.ru", null)
        );
        when(userService.getAllUsers()).thenReturn(users);

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Олег"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("Олег"));
    }

    @Test
    void getUserById_UserFound() throws Exception {
        User user = new User(1L, "Олег", "oleg@mail.ru", null);
        when(userService.getUserById(1L)).thenReturn(Optional.of(user));

        mockMvc.perform(get("/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Олег"))
                .andExpect(jsonPath("$.email").value("oleg@mail.ru"));
    }

    @Test
    void getUserById_UserNotFound() throws Exception {
        when(userService.getUserById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/users/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateUser_Success() throws Exception {
        User updatedUser = new User(1L, "Олег", "oleg@mail.ru", null);
        when(userService.updateUser(eq(1L), any(User.class))).thenReturn(updatedUser);

        mockMvc.perform(put("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Олег\", \"email\":\"oleg@mail.ru\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Олег"))
                .andExpect(jsonPath("$.email").value("oleg@mail.ru"));
    }

    @Test
    void deleteUser_Success() throws Exception {
        mockMvc.perform(delete("/users/1"))
                .andExpect(status().isNoContent());
    }

    // Views Tests
    @Test
    void getUserById_UserSummaryView() throws Exception {
        User user = new User(1L, "Олег", "oleg@mail.ru", null);
        when(userService.getUserById(1L)).thenReturn(Optional.of(user));

        mockMvc.perform(get("/users/1")
                        .queryParam("view", "summary"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Олег"))
                .andExpect(jsonPath("$.orders").doesNotExist());
    }

    @Test
    void getUserById_UserDetailsView() throws Exception {
        List<Order> orders = List.of(
                new Order(1L, "Delivered", 100, Arrays.asList("Item1", "Item2"), null)
        );
        User user = new User(1L, "Олег", "oleg@mail.ru", orders);
        when(userService.getUserById(1L)).thenReturn(Optional.of(user));

        mockMvc.perform(get("/users/1")
                        .queryParam("view", "details"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Олег"))
                .andExpect(jsonPath("$.orders[0].id").value(1))
                .andExpect(jsonPath("$.orders[0].status").value("Delivered"));
    }
}