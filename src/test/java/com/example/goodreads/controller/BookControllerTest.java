package com.example.goodreads.controller;

import com.example.goodreads.model.Book;
import com.example.goodreads.model.Author;
import com.example.goodreads.model.Publisher;
import com.example.goodreads.service.BookJpaService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import java.util.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookController.class)
public class BookControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookJpaService bookService;

    @Test
    void testGetBooks() throws Exception {
        Book book = new Book();
        book.setId(1);
        book.setName("Book1");
        when(bookService.getBooks()).thenReturn(new ArrayList<>(Collections.singletonList(book)));
        mockMvc.perform(MockMvcRequestBuilders.get("/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Book1"));
    }

    @Test
    void testGetBookByIdFound() throws Exception {
        Book book = new Book();
        book.setId(1);
        book.setName("Book1");
        when(bookService.getBookById(1)).thenReturn(book);
        mockMvc.perform(MockMvcRequestBuilders.get("/books/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Book1"));
    }

    @Test
    void testGetBookByIdNotFound() throws Exception {
        when(bookService.getBookById(1)).thenThrow(new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.NOT_FOUND));
        mockMvc.perform(MockMvcRequestBuilders.get("/books/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testAddBook() throws Exception {
        Book book = new Book();
        book.setId(1);
        book.setName("Book1");
        when(bookService.addBook(any(Book.class))).thenReturn(book);
        mockMvc.perform(MockMvcRequestBuilders.post("/publishers/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Book1\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Book1"));
    }

    @Test
    void testUpdateBook() throws Exception {
        Book book = new Book();
        book.setId(1);
        book.setName("UpdatedBook");
        when(bookService.updateBook(eq(1), any(Book.class))).thenReturn(book);
        mockMvc.perform(MockMvcRequestBuilders.put("/publishers/books/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"UpdatedBook\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("UpdatedBook"));
    }

    @Test
    void testDeleteBook() throws Exception {
        Mockito.doNothing().when(bookService).deleteBook(1);
        mockMvc.perform(MockMvcRequestBuilders.delete("/books/1"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetBookPublisher() throws Exception {
        Publisher publisher = new Publisher();
        publisher.setPublisherId(1);
        publisher.setPublisherName("Pub1");
        when(bookService.getBookPublisher(1)).thenReturn(publisher);
        mockMvc.perform(MockMvcRequestBuilders.get("/books/1/publisher"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.publisherName").value("Pub1"));
    }

    @Test
    void testGetBookAuthors() throws Exception {
        Author author = new Author();
        author.setAuthorId(1);
        author.setAuthorName("Author1");
        when(bookService.getBookAuthors(1)).thenReturn(Collections.singletonList(author));
        mockMvc.perform(MockMvcRequestBuilders.get("/books/1/authors"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].authorName").value("Author1"));
    }
} 