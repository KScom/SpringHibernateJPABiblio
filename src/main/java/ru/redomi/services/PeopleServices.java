package ru.redomi.services;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.redomi.models.Book;
import ru.redomi.models.Person;
import ru.redomi.repositories.PeopleRepositories;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class PeopleServices {

    private final PeopleRepositories peopleRepositories;

    @Autowired
    public PeopleServices(PeopleRepositories peopleRepositories) {
        this.peopleRepositories = peopleRepositories;
    }

    public List<Person> findAll(Integer page, Integer peoplePerPage, boolean sortByYear){
        if(sortByYear)
            return peopleRepositories.findAll(PageRequest.of(page,peoplePerPage, Sort.by("year"))).getContent();
        else
            return peopleRepositories.findAll(PageRequest.of(page,peoplePerPage)).getContent();
    }

    public List<Person> findAll(){
        return peopleRepositories.findAll();
    }

    public long countAll(){
        return peopleRepositories.count();
    }

    public Person findById(int id){
        Optional<Person> person = peopleRepositories.findById(id);
        return person.orElse(null);
    }

    public List<Book> getListBook(int id){
        Optional<Person> person = peopleRepositories.findById(id);
        Hibernate.initialize(person.get().getBooks());


        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -10);

        person.get().getBooks().forEach(book -> {
            long diffInMillies = Math.abs(book.getDateOfIssue().getTime() - new Date().getTime());
            // 864000000 милисекунд = 10 суток
            if (diffInMillies > 864000000)
                book.setExpired(true);

            System.out.println(book.isExpired());
        });


        return person.get().getBooks();
    }

    public void save(Person person){
        peopleRepositories.save(person);
    }

    public void delete(int id){
        peopleRepositories.deleteById(id);
    }

}
