package ru.redomi.models;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.util.Date;

@Entity
@Table(name = "books")
public class Book {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name")
    @Size(min = 2, max = 30,message = "Название книги должнобыть от 2 до 30 символов.")
    private String name;

    @Column(name = "author")
    @Size(min = 2, max = 30, message = "Название книги должно быть от 2 до 30 символов.")
    private String author;

    @Column(name = "year")
    @Min(value = 1900, message = "Го должен быть больше 1900.")
    private int year;

    @ManyToOne
    @JoinColumn(name = "person_id", referencedColumnName = "id")
    private Person person;

    @Column(name = "date_of_issue")
    private Date dateOfIssue;

    @Transient
    private boolean Expired;

    public Book(){}

    public Book(String name, String author, int year, Person person) {
        this.name = name;
        this.author = author;
        this.year = year;
        this.person = person;
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

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public Date getDateOfIssue() {
        return dateOfIssue;
    }

    public void setDateOfIssue(Date dateOfIssue) {
        this.dateOfIssue = dateOfIssue;
    }

    public boolean isExpired() {
        return Expired;
    }

    public void setExpired(boolean expired) {
        Expired = expired;
    }
}
