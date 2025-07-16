package com.example.goodreads.model;

import javax.persistence.*;
import java.util.*;
import com.example.goodreads.Book;

@Entity
@Table(name = "author")
public class Author {

    @Id
    @Column(name = "authorid")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int authorId;

    @Column(name = "authorname")
    private String authorName;

    @ManyToMany(mappedBy = "authors")
    private List<Book> books = new ArrayList<>();

    public Author(){

    }
    public Author(int authorid, String authorName){
        this.authorId = authorid;
        this.authorName = authorName;
    }

    public int getAuthorId(){
        return authorId;
    }
    public void setAuthorIdId(int authorId){
        this.authorId = authorId;
    }

    public String getAuthorName(){
        return authorName;
    }
    public void setAuthorName(String authorName){
        this.authorName = authorName;
    }

    public List<Book> getBooks(){
        return books;
    }

    public void setBooks(List<Books>){
        this.books = books;
    }

}