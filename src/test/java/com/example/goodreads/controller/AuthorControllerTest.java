package com.example.goodreads.controller;

import com.example.goodreads.model.Author;
import com.example.goodreads.service.AuthorJpaService;
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

@WebMvcTest(AuthorController.class)
public class AuthorControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthorJpaService authorService;

    @Test
    void testGetAuthors() throws Exception {
        Author author = new Author();
        author.setAuthorId(1);
        author.setAuthorName("Author1");
        when(authorService.getAuthors()).thenReturn(new ArrayList<>(Collections.singletonList(author)));
        mockMvc.perform(MockMvcRequestBuilders.get("/authors"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].authorName").value("Author1"));
    }

    @Test
    void testGetAuthorByIdFound() throws Exception {
        Author author = new Author();
        author.setAuthorId(1);
        author.setAuthorName("Author1");
        when(authorService.getAuthorById(1)).thenReturn(author);
        mockMvc.perform(MockMvcRequestBuilders.get("/authors/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.authorName").value("Author1"));
    }

    @Test
    void testGetAuthorByIdNotFound() throws Exception {
        when(authorService.getAuthorById(1)).thenThrow(new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.NOT_FOUND));
        mockMvc.perform(MockMvcRequestBuilders.get("/authors/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testAddAuthor() throws Exception {
        Author author = new Author();
        author.setAuthorId(1);
        author.setAuthorName("Author1");
        when(authorService.addAuthor(any(Author.class))).thenReturn(author);
        mockMvc.perform(MockMvcRequestBuilders.post("/authors")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"authorName\":\"Author1\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.authorName").value("Author1"));
    }
} 