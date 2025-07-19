package com.example.goodreads.controller;

import com.example.goodreads.model.Publisher;
import com.example.goodreads.service.PublisherJpaService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import java.util.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PublisherController.class)
public class PublisherControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PublisherJpaService publisherService;

    @Test
    void testGetPublishers() throws Exception {
        Publisher publisher = new Publisher();
        publisher.setPublisherId(1);
        publisher.setPublisherName("Publisher1");
        when(publisherService.getPublishers()).thenReturn(new ArrayList<>(Collections.singletonList(publisher)));
        mockMvc.perform(MockMvcRequestBuilders.get("/publishers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].publisherName").value("Publisher1"));
    }

    @Test
    void testGetPublisherByIdFound() throws Exception {
        Publisher publisher = new Publisher();
        publisher.setPublisherId(1);
        publisher.setPublisherName("Publisher1");
        when(publisherService.getPublisherById(1)).thenReturn(publisher);
        mockMvc.perform(MockMvcRequestBuilders.get("/publishers/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.publisherName").value("Publisher1"));
    }

    @Test
    void testGetPublisherByIdNotFound() throws Exception {
        when(publisherService.getPublisherById(1)).thenThrow(new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.NOT_FOUND));
        mockMvc.perform(MockMvcRequestBuilders.get("/publishers/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testAddPublisher() throws Exception {
        Publisher publisher = new Publisher();
        publisher.setPublisherId(1);
        publisher.setPublisherName("Publisher1");
        when(publisherService.addPublisher(any(Publisher.class))).thenReturn(publisher);
        mockMvc.perform(MockMvcRequestBuilders.post("/publishers")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"publisherName\":\"Publisher1\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.publisherName").value("Publisher1"));
    }
} 