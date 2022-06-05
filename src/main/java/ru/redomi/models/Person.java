package ru.redomi.models;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@Table(name = "person")
public class Person {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Size(min = 2, max = 30, message = "Имя должно быть от 2 до 30 символов.")
    @Column(name = "name")
    private String name;

    @Min(value = 1, message = "Возраст толжен быть больше нуля.")
    @Column(name = "year")
    private int year;

    @OneToMany(mappedBy = "person")
    private List<Book> books;

    public Person(){}

    public Person(String name, int year, List<Book> books) {
        this.name = name;
        this.year = year;
        this.books = books;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int age) {
        this.year = age;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }
}
