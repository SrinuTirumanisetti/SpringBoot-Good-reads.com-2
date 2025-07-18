package com.example.goodreads.controller;

import com.example.goodreads.model.Author;
import com.example.goodreads.model.Book;
import com.example.goodreads.service.AuthorJpaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class AuthorController {

    @Autowired
    private AuthorJpaService authorJpaService;

    @GetMapping("/authors")
   public ArrayList<Author> getAuthors(){
       return authorJpaService.getAuthors();
   }

   @GetMapping("/authors/{id}")
   public Author getAuthorById(@PathVariable("id") int id){
           return authorJpaService.getAuthorById(id);
   }

   @GetMapping("/authors/{authorId}/books")
    public List<Book> getAuthorBooks(@PathVariable("authorId") int authorId){
        return authorJpaService.getAuthorBooks(authorId);
    }   

   @PostMapping("/authors")
    public Author addAuthor(@RequestBody Author author){
        return authorJpaService.addAuthor(author);
    }

    @PutMapping("/authors/{id}")
    public Author updateAuthor(@RequestBody Author author, @PathVariable("id") int id) {
        return authorJpaService.updateAuthor(id, author);
    }

    @DeleteMapping("/authors/{id}")
    public void deleteAuthor(@PathVariable("id") int id) {
        authorJpaService.deleteAuthor(id);
    }
}