package com.example.goodreads.service;

import com.example.goodreads.model.Author;
import com.example.goodreads.model.Book;
import com.example.goodreads.repository.AuthorJpaRepository;
import com.example.goodreads.repository.BookJpaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthorJpaServiceTest {
    @InjectMocks
    private AuthorJpaService authorJpaService;

    @Mock
    private AuthorJpaRepository authorJpaRepository;
    @Mock
    private BookJpaRepository bookJpaRepository;

    @Test
    void testGetAuthorByIdFound() {
        Author author = new Author();
        author.setAuthorId(1);
        author.setAuthorName("Sample Author");
        when(authorJpaRepository.findById(1)).thenReturn(Optional.of(author));
        Author result = authorJpaService.getAuthorById(1);
        assertEquals("Sample Author", result.getAuthorName());
        verify(authorJpaRepository, times(1)).findById(1);
    }

    @Test
    void testGetAuthorByIdNotFound() {
        when(authorJpaRepository.findById(1)).thenReturn(Optional.empty());
        assertThrows(ResponseStatusException.class, () -> authorJpaService.getAuthorById(1));
        verify(authorJpaRepository, times(1)).findById(1);
    }

    @Test
    void testAddAuthorValid() {
        Book book = new Book();
        book.setId(1);
        Author author = new Author();
        author.setBooks(Collections.singletonList(book));
        when(bookJpaRepository.findAllById(anyList())).thenReturn(Collections.singletonList(book));
        when(authorJpaRepository.save(any(Author.class))).thenReturn(author);
        when(bookJpaRepository.saveAll(anyList())).thenReturn(Collections.singletonList(book));
        Author result = authorJpaService.addAuthor(author);
        assertNotNull(result);
        verify(bookJpaRepository, times(1)).findAllById(anyList());
        verify(authorJpaRepository, times(1)).save(any(Author.class));
        verify(bookJpaRepository, times(1)).saveAll(anyList());
    }

    @Test
    void testAddAuthorInvalidBooks() {
        Book book = new Book();
        book.setId(1);
        Author author = new Author();
        author.setBooks(Collections.singletonList(book));
        when(bookJpaRepository.findAllById(anyList())).thenReturn(Collections.emptyList());
        when(authorJpaRepository.save(any(Author.class))).thenReturn(author);
        when(bookJpaRepository.saveAll(anyList())).thenReturn(Collections.emptyList());
        Author result = authorJpaService.addAuthor(author);
        assertNotNull(result);
        assertTrue(result.getBooks().isEmpty());
        verify(bookJpaRepository, times(1)).findAllById(anyList());
        verify(authorJpaRepository, times(1)).save(any(Author.class));
        verify(bookJpaRepository, times(1)).saveAll(anyList());
    }

    @Test
    void testDeleteAuthorFound() {
        Author author = new Author();
        author.setAuthorId(1);
        author.setBooks(new ArrayList<>());
        when(authorJpaRepository.findById(1)).thenReturn(Optional.of(author));
        when(bookJpaRepository.saveAll(anyList())).thenReturn(Collections.emptyList());
        doNothing().when(authorJpaRepository).deleteById(1);
        assertThrows(ResponseStatusException.class, () -> authorJpaService.deleteAuthor(1));
        verify(authorJpaRepository, times(1)).findById(1);
        verify(bookJpaRepository, times(1)).saveAll(anyList());
        verify(authorJpaRepository, times(1)).deleteById(1);
    }

    @Test
    void testDeleteAuthorNotFound() {
        when(authorJpaRepository.findById(1)).thenReturn(Optional.empty());
        assertThrows(ResponseStatusException.class, () -> authorJpaService.deleteAuthor(1));
        verify(authorJpaRepository, times(1)).findById(1);
    }
} 