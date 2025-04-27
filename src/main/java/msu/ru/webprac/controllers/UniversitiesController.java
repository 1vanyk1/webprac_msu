package msu.ru.webprac.controllers;

import msu.ru.webprac.dao.UniversityDAO;
import msu.ru.webprac.dao.impl.UniversityDAOimpl;
import msu.ru.webprac.db.Universities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@Controller
public class UniversitiesController {
    @Autowired
    private final UniversityDAO universityDAO = new UniversityDAOimpl();

    @GetMapping("/universities")
    public String universitiesPage(Model model) {
        List<Universities> universities = (List<Universities>) universityDAO.getAll();
        Collections.sort(universities);
        model.addAttribute("universities", universities);
        model.addAttribute("universitiesService", universityDAO);
        return "universities";
    }

    @GetMapping("/add_university")
    public String addUniversity(Model model) {
        Universities university = new Universities();
        university.setId((long) -1);
        model.addAttribute("university", university);
        model.addAttribute("universitiesService", universityDAO);
        return "edit_university";
    }

    @GetMapping("/edit_university")
    public String universityPage(@RequestParam(name = "id") Long id, Model model) {
        Universities university = universityDAO.getById(id);
        if (university == null) {
            model.addAttribute("error_msg", "Нет университета с id=" + id);
            return "error_page";
        }
        model.addAttribute("university", university);
        model.addAttribute("universitiesService", universityDAO);
        return "edit_university";
    }

    @PostMapping("/save_university")
    public String saveUniversity(Universities in,
                                 Model model) {
        if (in.getId() == -1) {
            in.setId(universityDAO.getMaxId() + 1);
            universityDAO.insert(in);
            return "redirect:/universities";
        }
        Universities university = universityDAO.getById(in.getId());
        if (university != null) {
            universityDAO.update(in.getId(), in);
            return "redirect:/universities";
        }

        model.addAttribute("error_msg", "Невозможно сохранить данные");
        return "error_page";
    }

    @PostMapping("/remove_university")
    public String removeUniversity(@RequestParam(name = "id") Long id,
                                   Model model) {
        universityDAO.delete(id);
        return "redirect:/universities";
    }

    @GetMapping("/remove_university")
    public String removeUniversityGet(@RequestParam(name = "id") Long id,
                                   Model model) {
        return removeUniversity(id, model);
    }
}
