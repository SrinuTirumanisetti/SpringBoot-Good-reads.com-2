package com.example.goodreads.service;

import com.example.goodreads.model.Book;
import com.example.goodreads.model.Author;
import com.example.goodreads.model.Publisher;
import com.example.goodreads.repository.BookJpaRepository;
import com.example.goodreads.repository.AuthorJpaRepository;
import com.example.goodreads.repository.PublisherJpaRepository;
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
public class BookJpaServiceTest {
    @InjectMocks
    private BookJpaService bookJpaService;

    @Mock
    private BookJpaRepository bookJpaRepository;
    @Mock
    private AuthorJpaRepository authorJpaRepository;
    @Mock
    private PublisherJpaRepository publisherJpaRepository;

    @Test
    void testGetBookByIdFound() {
        Book book = new Book();
        book.setId(1);
        book.setName("Sample Book");
        when(bookJpaRepository.findById(1)).thenReturn(Optional.of(book));
        Book result = bookJpaService.getBookById(1);
        assertEquals("Sample Book", result.getName());
        verify(bookJpaRepository, times(1)).findById(1);
    }

    @Test
    void testGetBookByIdNotFound() {
        when(bookJpaRepository.findById(1)).thenReturn(Optional.empty());
        assertThrows(ResponseStatusException.class, () -> bookJpaService.getBookById(1));
        verify(bookJpaRepository, times(1)).findById(1);
    }

    @Test
    void testAddBookValid() {
        Author author = new Author();
        author.setAuthorId(1);
        Publisher publisher = new Publisher();
        publisher.setPublisherId(1);
        Book book = new Book();
        book.setAuthors(Collections.singletonList(author));
        book.setPublisher(publisher);
        when(authorJpaRepository.findAllById(anyList())).thenReturn(Collections.singletonList(author));
        when(publisherJpaRepository.findById(1)).thenReturn(Optional.of(publisher));
        when(bookJpaRepository.save(any(Book.class))).thenReturn(book);
        Book result = bookJpaService.addBook(book);
        assertNotNull(result);
        verify(authorJpaRepository, times(1)).findAllById(anyList());
        verify(publisherJpaRepository, times(1)).findById(1);
        verify(bookJpaRepository, times(1)).save(any(Book.class));
    }

    @Test
    void testAddBookInvalidAuthor() {
        Author author = new Author();
        author.setAuthorId(1);
        Publisher publisher = new Publisher();
        publisher.setPublisherId(1);
        Book book = new Book();
        book.setAuthors(Collections.singletonList(author));
        book.setPublisher(publisher);
        when(authorJpaRepository.findAllById(anyList())).thenReturn(Collections.emptyList());
        assertThrows(ResponseStatusException.class, () -> bookJpaService.addBook(book));
    }

    @Test
    void testAddBookInvalidPublisher() {
        Author author = new Author();
        author.setAuthorId(1);
        Publisher publisher = new Publisher();
        publisher.setPublisherId(1);
        Book book = new Book();
        book.setAuthors(Collections.singletonList(author));
        book.setPublisher(publisher);
        when(authorJpaRepository.findAllById(anyList())).thenReturn(Collections.singletonList(author));
        when(publisherJpaRepository.findById(1)).thenReturn(Optional.empty());
        assertThrows(ResponseStatusException.class, () -> bookJpaService.addBook(book));
    }

    @Test
    void testDeleteBookFound() {
        when(bookJpaRepository.existsById(1)).thenReturn(true);
        doNothing().when(bookJpaRepository).deleteById(1);
        assertDoesNotThrow(() -> bookJpaService.deleteBook(1));
        verify(bookJpaRepository, times(1)).existsById(1);
        verify(bookJpaRepository, times(1)).deleteById(1);
    }

    @Test
    void testDeleteBookNotFound() {
        when(bookJpaRepository.existsById(1)).thenReturn(false);
        assertThrows(ResponseStatusException.class, () -> bookJpaService.deleteBook(1));
        verify(bookJpaRepository, times(1)).existsById(1);
    }
}
