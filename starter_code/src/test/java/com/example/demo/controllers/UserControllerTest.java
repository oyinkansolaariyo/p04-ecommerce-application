package com.example.demo.controllers;


import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.*;

import java.net.URI;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
@SpringBootTest
@WebAppConfiguration
public class UserControllerTest {

    private MockMvc mvc;

    @Autowired
    private WebApplicationContext context;


    @Autowired
    UserRepository userRepository;

    @Autowired
    CartRepository cartRepository;

    @Before
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    @WithMockUser(value = "spring")
    public void testCreateUser() throws Exception {
        String userCreateJson = "{ \"username\":\"oyin\", \"password\": \"oyinkansola\", \"confirmPassword\": \"oyinkansola\" }";
        mvc.perform(
                post(new URI("/api/user/create"))
                        .content(userCreateJson)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isCreated());
    }


    @Test
    @WithMockUser(value = "spring")
    public void testFindByUserName() throws Exception {
        String username = "seyi";
        User user = new User();
        user.setPassword("password");
        user.setUsername(username);
        user.setCart(new Cart());
        userRepository.save(user);
        mvc.perform(
                get(new URI("/api/user/" + username))
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                        .andExpect(MockMvcResultMatchers.jsonPath("$.username").value(username)
        );
    }

    @Test
    @WithMockUser(value = "spring")
    public void testFindById() throws Exception {
        User user = new User();
        user.setPassword("password");
        user.setUsername("tit");
        user.setCart(new Cart());
        User savedUser = userRepository.save(user);
        mvc.perform(
                get(new URI("/api/user/id/" + savedUser.getId()))
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.username").value(savedUser.getUsername()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(savedUser.getId())
                );
    }

}
