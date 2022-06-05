package ru.redomi.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.redomi.models.Person;

public interface PeopleRepositories extends JpaRepository<Person,Integer> {
    long count();
}
