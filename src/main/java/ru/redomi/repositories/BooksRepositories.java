package ru.redomi.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.redomi.models.Book;

import java.util.List;

public interface BooksRepositories extends JpaRepository<Book, Integer> {
    long count();
    List<Book> findByNameContaining(String name);
}
