package msu.ru.webprac.controllers;

import msu.ru.webprac.dao.ProfessorDAO;
import msu.ru.webprac.dao.UniversityDAO;
import msu.ru.webprac.dao.impl.ProfessorDAOimpl;
import msu.ru.webprac.dao.impl.UniversityDAOimpl;
import msu.ru.webprac.db.Lectures;
import msu.ru.webprac.db.Professors;
import msu.ru.webprac.db.Universities;
import msu.ru.webprac.db.datas.TimeInterval;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.Date;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

@Controller
public class ProfessorsController {
    @Autowired
    private final ProfessorDAO professorDAO = new ProfessorDAOimpl();

    @Autowired
    private final UniversityDAO universityDAO = new UniversityDAOimpl();

    @GetMapping("/professors")
    public String professorsPage(Model model) {
        List<Professors> professors = (List<Professors>) professorDAO.getAll();
        Collections.sort(professors);
        model.addAttribute("professors", professors);
        model.addAttribute("professorsService", professorDAO);
        return "professors";
    }

    @GetMapping("/professor")
    public String professorPage(@RequestParam(name = "id") Long id,
                                @RequestParam(name = "from") @Nullable Date from,
                                @RequestParam(name = "to") @Nullable Date to,
                                Model model) {
        Professors professor = professorDAO.getById(id);
        if (professor == null) {
            model.addAttribute("error_msg", "Нет профессора с id=" + id);
            return "error_page";
        }
        model.addAttribute("professor", professor);
        model.addAttribute("professorsService", professorDAO);

        List<Lectures> lectures = professorDAO.getLecturesForProfessor(id);
        Collections.sort(lectures);
        TimeInterval ti;
        if (from != null && to != null) {
            ti = new TimeInterval(from, to);
        } else {
            Calendar c = Calendar.getInstance();
            c.setTime(new Date(System.currentTimeMillis()));
            c.add(Calendar.DATE, c.getTime().getDay());
            if (c.getTime().getDay() == 0) {
                c.add(Calendar.DATE, -6);
            } else {
                c.add(Calendar.DATE, 1 - c.getTime().getDay());
            }
            Date d1 = new Date(c.getTime().getTime());
            c.add(Calendar.DATE, 6);
            Date d2 = new Date(c.getTime().getTime());
            ti = new TimeInterval(d1, d2);
        }
        lectures.removeIf(e -> !(
                ti.getFrom().getTime() <= e.getDate().getTime() &&
                        e.getDate().getTime() <= ti.getTo().getTime()
        ));

        model.addAttribute("lectures", lectures);
        model.addAttribute("time_interval", ti);

        return "professor";
    }

    @PostMapping("/professor")
    public String professorPagePost(Long id, TimeInterval ti,
                                    Model model) {
        return professorPage(id, ti.getFrom(), ti.getTo(), model);
    }

    @GetMapping("/add_professor")
    public String addProfessor(Model model) {
        Professors professor = new Professors();
        professor.setId((long) -1);
        model.addAttribute("professor", professor);
        model.addAttribute("professorsService", professorDAO);
        List<Universities> universities = universityDAO.getAll();
        model.addAttribute("universities", universities);
        return "edit_professor";
    }

    @GetMapping("/edit_professor")
    public String editProfessorPage(@RequestParam(name = "id") Long id, Model model) {
        Professors professor = professorDAO.getById(id);
        if (professor == null) {
            model.addAttribute("error_msg", "Нет лектора с id=" + id);
            return "error_page";
        }
        model.addAttribute("professor", professor);
        model.addAttribute("professorsService", professorDAO);
        List<Universities> universities = universityDAO.getAll();
        model.addAttribute("universities", universities);
        return "edit_professor";
    }

    @PostMapping("/save_professor")
    public String saveProfessor(Professors in,
                                Model model) {
        if (in.getUniversity() == null) {
            model.addAttribute("error_msg", "Не выбран университет");
            return "error_page";
        }
        if (in.getId() == -1) {
            in.setId(professorDAO.getMaxId() + 1);
            professorDAO.insert(in);
            return "redirect:/professors";
        }
        Professors professor = professorDAO.getById(in.getId());
        in.setUniversity(universityDAO.getById(in.getUniversity().getId()));
        if (professor != null) {
            professorDAO.update(in.getId(), in);
            return "redirect:/professors";
        }

        model.addAttribute("error_msg", "Невозможно сохранить данные");
        return "error_page";
    }

    @PostMapping("/remove_professor")
    public String removeProfessor(@RequestParam(name = "id") Long id,
                                Model model) {
        professorDAO.delete(id);
        return "redirect:/professors";
    }

    @GetMapping("/remove_professor")
    public String removeProfessorGet(@RequestParam(name = "id") Long id,
                                   Model model) {
        return removeProfessor(id, model);
    }
}
