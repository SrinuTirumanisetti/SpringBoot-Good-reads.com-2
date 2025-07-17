package com.example.goodreads.service;

import com.example.goodreads.model.Author;
import com.example.goodreads.model.Book;
import com.example.goodreads.repository.AuthorJpaRepository;
import com.example.goodreads.repository.BookJpaRepository;
import com.example.goodreads.repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import com.example.goodreads.model.Book;
import java.util.NoSuchElementException;

import java.util.ArrayList;
import java.util.List;

@Service
public class AuthorJpaService implements AuthorRepository {

    @Autowired
    private AuthorJpaRepository authorJpaRepository;

    @Autowired
    private BookJpaRepository bookJpaRepository;

    @Override
    public ArrayList<Author> getAuthors() {
        List<Author> authorList = authorJpaRepository.findAll();
        ArrayList<Author> authors = new ArrayList<>(authorList);
        return authors;
    }

    @Override
    public Author getAuthorById(int authorId) {
        try {
            Author author = authorJpaRepository.findById(authorId).get();
            return author;
        }catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public Author addAuthor(Author author) {
        List<Integer> bookIds = new ArrayList<>();
        for (Book book : author.getBooks()) {
            bookIds.add(book.getId());
        }

        List<Book> books = bookJpaRepository.findAllById(bookIds);

        author.setBooks(books);

        for (Book book : books) {
            book.getAuthors().add(author);
        }

        Author savedAuthor = authorJpaRepository.save(author);

        bookJpaRepository.saveAll(books);

        return savedAuthor;
    }

    @Override
    public Author updateAuthor(int authorId, Author author) {
        try {
            Author newAuthor = authorJpaRepository.findById(authorId).get();
            if (author.getAuthorName() != null) {
                newAuthor.setAuthorName(author.getAuthorName());
            }
            if (author.getBooks() != null) {
                List<Book> books = newAuthor.getBooks();
                for (Book book : books) {
                    book.getAuthors().remove(newAuthor);
                }
                bookJpaRepository.saveAll(books);

                List<Integer> newBookIds = new ArrayList<>();
                for (Book book : author.getBooks()) {
                    newBookIds.add(book.getId());
                }

                List<Book> newBooks = bookJpaRepository.findAllById(newBookIds);

                for (Book book : newBooks) {
                    book.getAuthors().add(newAuthor);
                }
                bookJpaRepository.saveAll(newBooks);

                newAuthor.setBooks(newBooks);
            }
            return authorJpaRepository.save(newAuthor);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public List<Book> getAuthorBooks(int authorId) {
        try {
            Author author = authorJpaRepository.findById(authorId).get();
            return author.getBooks();
        }catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public void deleteAuthor(int authorId) {
        try {
                Author author = authorJpaRepository.findById(authorId).get();
                List<Book> books = author.getBooks();
                for(Book book:books){
                    book.getAuthors().remove(author);
                }
                bookJpaRepository.saveAll(books);
                authorJpaRepository.deleteById(authorId);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        throw new ResponseStatusException(HttpStatus.NO_CONTENT);
    }
}