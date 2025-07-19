package com.example.goodreads.service;

import com.example.goodreads.model.Publisher;
import com.example.goodreads.model.Book;
import com.example.goodreads.repository.PublisherJpaRepository;
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
public class PublisherJpaServiceTest {
    @InjectMocks
    private PublisherJpaService publisherJpaService;

    @Mock
    private PublisherJpaRepository publisherJpaRepository;
    @Mock
    private BookJpaRepository bookJpaRepository;

    @Test
    void testGetPublisherByIdFound() {
        Publisher publisher = new Publisher();
        publisher.setPublisherId(1);
        publisher.setPublisherName("Sample Publisher");
        when(publisherJpaRepository.findById(1)).thenReturn(Optional.of(publisher));
        Publisher result = publisherJpaService.getPublisherById(1);
        assertEquals("Sample Publisher", result.getPublisherName());
        verify(publisherJpaRepository, times(1)).findById(1);
    }

    @Test
    void testGetPublisherByIdNotFound() {
        when(publisherJpaRepository.findById(1)).thenReturn(Optional.empty());
        assertThrows(ResponseStatusException.class, () -> publisherJpaService.getPublisherById(1));
        verify(publisherJpaRepository, times(1)).findById(1);
    }

    @Test
    void testAddPublisher() {
        Publisher publisher = new Publisher();
        publisher.setPublisherId(1);
        publisher.setPublisherName("Sample Publisher");
        when(publisherJpaRepository.save(any(Publisher.class))).thenReturn(publisher);
        Publisher result = publisherJpaService.addPublisher(publisher);
        assertNotNull(result);
        verify(publisherJpaRepository, times(1)).save(any(Publisher.class));
    }

    @Test
    void testUpdatePublisherFound() {
        Publisher publisher = new Publisher();
        publisher.setPublisherId(1);
        publisher.setPublisherName("Updated Publisher");
        when(publisherJpaRepository.findById(1)).thenReturn(Optional.of(publisher));
        when(publisherJpaRepository.save(any(Publisher.class))).thenReturn(publisher);
        Publisher result = publisherJpaService.updatePublisher(1, publisher);
        assertNotNull(result);
        verify(publisherJpaRepository, times(1)).findById(1);
        verify(publisherJpaRepository, times(1)).save(any(Publisher.class));
    }

    @Test
    void testUpdatePublisherNotFound() {
        Publisher publisher = new Publisher();
        publisher.setPublisherId(1);
        when(publisherJpaRepository.findById(1)).thenReturn(Optional.empty());
        assertThrows(ResponseStatusException.class, () -> publisherJpaService.updatePublisher(1, publisher));
        verify(publisherJpaRepository, times(1)).findById(1);
    }

    @Test
    void testDeletePublisherFound() {
        Publisher publisher = new Publisher();
        publisher.setPublisherId(1);
        when(publisherJpaRepository.findById(1)).thenReturn(Optional.of(publisher));
        when(bookJpaRepository.findByPublisher(publisher)).thenReturn(new ArrayList<>());
        when(bookJpaRepository.saveAll(anyList())).thenReturn(Collections.emptyList());
        doNothing().when(publisherJpaRepository).deleteById(1);
        assertThrows(ResponseStatusException.class, () -> publisherJpaService.deletePublisher(1));
        verify(publisherJpaRepository, times(1)).findById(1);
        verify(bookJpaRepository, times(1)).findByPublisher(publisher);
        verify(bookJpaRepository, times(1)).saveAll(anyList());
        verify(publisherJpaRepository, times(1)).deleteById(1);
    }

    @Test
    void testDeletePublisherNotFound() {
        when(publisherJpaRepository.findById(1)).thenReturn(Optional.empty());
        assertThrows(ResponseStatusException.class, () -> publisherJpaService.deletePublisher(1));
        verify(publisherJpaRepository, times(1)).findById(1);
    }
} 