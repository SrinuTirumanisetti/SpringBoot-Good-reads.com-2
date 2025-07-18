package com.example.goodreads.repository;

import com.example.goodreads.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.goodreads.model.Publisher;
import java.util.*;

@Repository
public interface BookJpaRepository extends JpaRepository<Book, Integer> {
    ArrayList<Book> findByPublisher(Publisher publisher);
}