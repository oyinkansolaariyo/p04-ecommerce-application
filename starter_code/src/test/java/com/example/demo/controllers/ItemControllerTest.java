package com.example.demo.controllers;

import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
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

import java.math.BigDecimal;
import java.net.URI;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
@SpringBootTest
@WebAppConfiguration
public class ItemControllerTest {
    private MockMvc mvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    ItemRepository itemRepository;

    @Before
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    public Item createItem(){
        Item item = new Item();
        item.setName("Book");
        item.setPrice(new BigDecimal("20.0"));
        item.setDescription("A book");
        return itemRepository.save(item);
    }


    @Test
    @WithMockUser(value = "spring")
    public  void  testGetItems() throws Exception {
        itemRepository.deleteAll();
        Item item = createItem();
        mvc.perform(
                get(new URI("/api/item"))
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$..id").value((item.getId().intValue()))
                );
    }

    @Test
    @WithMockUser(value = "spring")
    public void testGetItemById() throws Exception {
        itemRepository.deleteAll();
        Item item = createItem();
        mvc.perform(
                get(new URI("/api/item/" + item.getId()))
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                        .andExpect(MockMvcResultMatchers.jsonPath("$.id").value((item.getId().intValue()))
                );

    }

    @Test
    @WithMockUser(value = "spring")
    public void testGetItemsByName() throws Exception {
        itemRepository.deleteAll();
        Item item = createItem();
        mvc.perform(
                get(new URI("/api/item/name/" + item.getName()))
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$..id").value((item.getId().intValue()))
                );
    }

}
