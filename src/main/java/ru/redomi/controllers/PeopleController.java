package ru.redomi.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.redomi.models.Book;
import ru.redomi.models.Person;
import ru.redomi.services.PeopleServices;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/people")
public class PeopleController {

    private final PeopleServices peopleServices;

    @Autowired
    public PeopleController(PeopleServices peopleServices) {
        this.peopleServices = peopleServices;
    }

    @GetMapping
    public String index(Model model,
                        @RequestParam(value = "page", required = false) Integer page,
                        @RequestParam(value = "people_per_page", required = false) Integer peoplePerPage,
                        @RequestParam(value = "sort_by_year", required = false) boolean sortByYear){

        if(page==null) page=0;
        if(peoplePerPage==null) peoplePerPage=3;

        model.addAttribute("sortByYear", sortByYear);
        model.addAttribute("currentPage",page);
        model.addAttribute("peoplePerPage",peoplePerPage);
        model.addAttribute("peopleCount", peopleServices.countAll());
        model.addAttribute("people",peopleServices.findAll(page, peoplePerPage, sortByYear));
        return "people/index";
    }

    @GetMapping("/{id}")
    public String show(Model model,
                       @PathVariable("id") int id){

        Person person = peopleServices.findById(id);
        List<Book> books = peopleServices.getListBook(id);

        if(books.size() > 0)
            model.addAttribute("books",books);

        model.addAttribute("person",person);
        return "people/show";
    }

    @GetMapping("/new")
    public String newPerson(@ModelAttribute("person") Person person){
        return "people/new";
    }

    @PostMapping
    @Transactional
    public String save(@ModelAttribute("person") @Valid Person person,
                     BindingResult bindingResult){

        if(bindingResult.hasErrors())
            return "people/new";

        peopleServices.save(person);
        return "redirect:people/";
    }
    
    @GetMapping("/{id}/edit")
    public String edit(Model model,@PathVariable("id") int id){
        model.addAttribute("person",peopleServices.findById(id));
        return "people/edit";
    }

    @PatchMapping("/{id}/edit")
    @Transactional
    public String update(@ModelAttribute("person") @Valid Person person,
                         BindingResult bindingResult,
                         @PathVariable("id") int id){

        if(bindingResult.hasErrors())
            return "people/edit";

        peopleServices.save(person);
        return "redirect:/people/" + id;
    }

    @DeleteMapping("/{id}")
    @Transactional
    public String delete(@PathVariable("id") int id){
        peopleServices.delete(id);
        return "redirect:/people";
    }


}
