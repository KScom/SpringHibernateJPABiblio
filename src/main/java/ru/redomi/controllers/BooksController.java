package ru.redomi.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.redomi.models.Book;
import ru.redomi.models.Person;
import ru.redomi.services.BookServices;
import ru.redomi.services.PeopleServices;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/books")
public class BooksController {

    private final BookServices bookServices;
    private final PeopleServices peopleServices;

    @Autowired
    public BooksController(BookServices bookServices, PeopleServices peopleServices) {
        this.bookServices = bookServices;
        this.peopleServices = peopleServices;
    }

    @GetMapping
    public String index(Model model,
                        @RequestParam(value = "page", required = false) Integer page,
                        @RequestParam(value = "books_per_page", required = false) Integer booksPerPage,
                        @RequestParam(value = "sort_by_year", required = false) boolean sortByYear){

        if(page==null) page=0;
        if(booksPerPage==null) booksPerPage=3;

        model.addAttribute("sortByYear", sortByYear);
        model.addAttribute("currentPage",page);
        model.addAttribute("booksPerPage",booksPerPage);
        model.addAttribute("booksCount", bookServices.countAll());
        model.addAttribute("books",bookServices.findAll(page,booksPerPage,sortByYear));
        return "books/index";
    }

    @GetMapping("/{id}")
    public String show(Model model, @ModelAttribute("person") Person person,
                       @PathVariable("id") int id){

        Book book = bookServices.findById(id);
        model.addAttribute("books",book);

        if(book.getPerson()==null)
            model.addAttribute("people", peopleServices.findAll());
        else
            model.addAttribute("owner",book.getPerson());

        return "books/show";
    }

    @PatchMapping("/{id}/assign")
    @Transactional
    public String newOwner(@PathVariable("id") int id,
                           @ModelAttribute("person")Person person){

        bookServices.addOwner(person,id);

        return "redirect:/books/" + id;
    }

    @PatchMapping("/{id}/return")
    @Transactional
    public String deleteOwner(@PathVariable("id") int id){
        bookServices.deleteOwner(id);
        return "redirect:/books/" + id;
    }

    @GetMapping("/new")
    public String newBook(@ModelAttribute("book") Book book){
        return "books/new";
    }

    @PostMapping
    @Transactional
    public String save(@ModelAttribute("book") @Valid Book book,
                       BindingResult bindingResult){

        if(bindingResult.hasErrors())
            return "books/new";

        bookServices.save(book);
        return "redirect:/books";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id){
        model.addAttribute("book",bookServices.findById(id));
        return "books/edit";
    }

    @PatchMapping("/{id}/edit")
    @Transactional
    public String update(@ModelAttribute("book") @Valid Book book,
                         BindingResult bindingResult,
                         @PathVariable("id") int id){

        if(bindingResult.hasErrors())
            return "books/edit";

        bookServices.save(book);
        return "redirect:/books/" + id;
    }

    @DeleteMapping("/{id}")
    @Transactional
    public String delete(@PathVariable("id") int id){
        bookServices.delete(id);
        return "redirect:/books";
    }

    @GetMapping("/search")
    public String search(){
        return "books/search";
    }

    @PostMapping("/search")
    public String searchResult(Model model, @RequestParam("search") String search){

        List<Book> result = bookServices.search(search);

        if(result.size()>0)
            model.addAttribute("result",result);
        else
            model.addAttribute("noFound","По вашему запросу ничего не найдено.");

        return "books/search";
    }

}
