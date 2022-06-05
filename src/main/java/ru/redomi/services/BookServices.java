package ru.redomi.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.redomi.models.Book;
import ru.redomi.models.Person;
import ru.redomi.repositories.BooksRepositories;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class BookServices {

    private final BooksRepositories booksRepositories;

    @Autowired
    public BookServices(BooksRepositories booksRepositories) {
        this.booksRepositories = booksRepositories;
    }

    public List<Book> findAll(Integer page, Integer booksPerPage, boolean sortByYear){
        if(sortByYear)
            return booksRepositories.findAll(PageRequest.of(page,booksPerPage,Sort.by("year"))).getContent();
        else
            return booksRepositories.findAll(PageRequest.of(page,booksPerPage)).getContent();
    }

    public long countAll(){
        return booksRepositories.count();
    }

    public Book findById(int id){
        Optional<Book> book = booksRepositories.findById(id);
        return book.orElse(null);
    }

    public void save(Book book){
        booksRepositories.save(book);
    }

    public void delete(int id){
        booksRepositories.deleteById(id);
    }

    public void addOwner(Person person, int id){
        Book book = booksRepositories.findById(id).orElse(null);
        book.setDateOfIssue(new Date());
        book.setPerson(person);
    }

    public void deleteOwner(int id){
        Book book = booksRepositories.findById(id).orElse(null);
        book.setDateOfIssue(null);
        book.setPerson(null);
    }

    public List<Book> search(String search){
        return booksRepositories.findByNameContaining(search);
    }

}
