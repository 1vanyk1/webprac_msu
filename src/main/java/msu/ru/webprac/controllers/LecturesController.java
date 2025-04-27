package msu.ru.webprac.controllers;

import msu.ru.webprac.dao.CourseDAO;
import msu.ru.webprac.dao.LectureDAO;
import msu.ru.webprac.dao.ProfessorDAO;
import msu.ru.webprac.dao.impl.CourseDAOimpl;
import msu.ru.webprac.dao.impl.LectureDAOimpl;
import msu.ru.webprac.dao.impl.ProfessorDAOimpl;
import msu.ru.webprac.db.*;
import msu.ru.webprac.db.datas.LecturesForm;
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
public class LecturesController {
    @Autowired
    private final LectureDAO lectureDAO = new LectureDAOimpl();

    @Autowired
    private final ProfessorDAO professorDAO = new ProfessorDAOimpl();

    @Autowired
    private final CourseDAO courseDAO = new CourseDAOimpl();

    @GetMapping("/schedule")
    public String lecturesPage(@RequestParam(name = "from") @Nullable Date from,
                               @RequestParam(name = "to") @Nullable Date to,
                               Model model) {
        List<Lectures> lectures = (List<Lectures>) lectureDAO.getAll();
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
        model.addAttribute("lecturesService", lectureDAO);

        model.addAttribute("time_interval", ti);
        return "schedule";
    }

    @PostMapping("/schedule")
    public String lecturesPagePost(TimeInterval ti,
                                   Model model) {
        return lecturesPage(ti.getFrom(), ti.getTo(), model);
    }

    @GetMapping("/add_lecture")
    public String addLecture(Model model) {
        LecturesForm lecture = new LecturesForm();
        lecture.setId((long) -1);
        model.addAttribute("lecture", lecture);
        model.addAttribute("lecturesService", lectureDAO);
        List<Courses> courses = courseDAO.getAll();
        model.addAttribute("courses", courses);
        List<Professors> professors = professorDAO.getAll();
        model.addAttribute("professors", professors);
        return "edit_lecture";
    }

    @GetMapping("/edit_lecture")
    public String lecturePage(@RequestParam(name = "id") Long id, Model model) {
        Lectures lecture = lectureDAO.getById(id);
        if (lecture == null) {
            model.addAttribute("error_msg", "Нет лекции с id=" + id);
            return "error_page";
        }
        LecturesForm form = new LecturesForm();
        form.replace(lecture);
        model.addAttribute("lecture", form);
        model.addAttribute("lecturesService", lectureDAO);
        List<Courses> courses = courseDAO.getAll();
        model.addAttribute("courses", courses);
        List<Professors> professors = professorDAO.getAll();
        model.addAttribute("professors", professors);
        return "edit_lecture";
    }

    @PostMapping("/save_lecture")
    public String saveLecture(LecturesForm form,
                              Model model) {
        Lectures in = new Lectures();
        in.replace(form);
        if (in.getId() == -1) {
            in.setId(lectureDAO.getMaxId() + 1);
            lectureDAO.insert(in);
            return "redirect:/schedule";
        }
        Lectures lecture = lectureDAO.getById(in.getId());
        in.setCourse(lecture.getCourse());
        in.setProfessor(lecture.getProfessor());
        if (lecture != null) {
            lectureDAO.update(in.getId(), in);
            return "redirect:/schedule";
        }

        model.addAttribute("error_msg", "Невозможно сохранить данные");
        return "error_page";
    }

    @PostMapping("/remove_lecture")
    public String removeLecture(@RequestParam(name = "id") Long id,
                                   Model model) {
        lectureDAO.delete(id);
        return "redirect:/schedule";
    }

    @GetMapping("/remove_lecture")
    public String removeLectureGet(@RequestParam(name = "id") Long id,
                                   Model model) {
        return removeLecture(id, model);
    }
}
