package com.example.goodreads.service;

import com.example.goodreads.model.Book;
import com.example.goodreads.model.Publisher;
import com.example.goodreads.model.Author;
import com.example.goodreads.repository.BookJpaRepository;
import com.example.goodreads.repository.AuthorJpaRepository;
import com.example.goodreads.repository.PublisherJpaRepository;
import com.example.goodreads.repository.BookRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
public class BookJpaService implements BookRepository {

    @Autowired
    private BookJpaRepository bookJpaRepository;

    @Autowired
    private PublisherJpaRepository publisherJpaRepository;

    @Autowired
    private AuthorJpaRepository authorJpaRepository;

    @Override
    public ArrayList<Book> getBooks() {
        List<Book> bookList = bookJpaRepository.findAll();
        return new ArrayList<>(bookList);
    }

    @Override
    public Book getBookById(int bookId) {
        return bookJpaRepository.findById(bookId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @Override
    public Book addBook(Book book) {
        List<Integer> authorIds = new ArrayList<>();
        for (Author author : book.getAuthors()) {
            authorIds.add(author.getAuthorId());
        }

        int publisherId = book.getPublisher().getPublisherId();

        List<Author> complete_authors = authorJpaRepository.findAllById(authorIds);
        if (authorIds.size() != complete_authors.size()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "One or more author ids are invalid");
        }

        Publisher publisher = publisherJpaRepository.findById(publisherId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Wrong publisherId"));

        book.setAuthors(complete_authors);
        book.setPublisher(publisher);
        return bookJpaRepository.save(book);
    }

    @Override
    public Book updateBook(int bookId, Book book) {
        Book newBook = bookJpaRepository.findById(bookId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (book.getName() != null) {
            newBook.setName(book.getName());
        }
        if (book.getImageUrl() != null) {
            newBook.setImageUrl(book.getImageUrl());
        }
        if (book.getPublisher() != null) {
            int publisherId = book.getPublisher().getPublisherId();
            Publisher newPublisher = publisherJpaRepository.findById(publisherId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Wrong publisherId"));
            newBook.setPublisher(newPublisher);
        }

        return bookJpaRepository.save(newBook);
    }

    @Override
    public void deleteBook(int bookId) {
        if (!bookJpaRepository.existsById(bookId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        bookJpaRepository.deleteById(bookId);
    }

    @Override
    public Publisher getBookPublisher(int bookId) {
        Book book = bookJpaRepository.findById(bookId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return book.getPublisher();
    }

    @Override
    public List<Author> getBookAuthors(int bookId) {
        Book book = bookJpaRepository.findById(bookId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return book.getAuthors();
    }
}
