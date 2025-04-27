package msu.ru.webprac.controllers;

import msu.ru.webprac.dao.CourseDAO;
import msu.ru.webprac.dao.StudentDAO;
import msu.ru.webprac.dao.impl.CourseDAOimpl;
import msu.ru.webprac.dao.impl.StudentDAOimpl;
import msu.ru.webprac.db.Courses;
import msu.ru.webprac.db.Lectures;
import msu.ru.webprac.db.Students;
import msu.ru.webprac.db.datas.StudentCourse;
import msu.ru.webprac.db.datas.TimeInterval;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.Date;
import java.util.*;

@Controller
public class StudentsController {
    @Autowired
    private final StudentDAO studentDAO = new StudentDAOimpl();

    @Autowired
    private final CourseDAO courseDAO = new CourseDAOimpl();

    @GetMapping("/students")
    public String studentsPage(Model model) {
        List<Students> students = (List<Students>) studentDAO.getAll();
        Collections.sort(students);
        model.addAttribute("students", students);
        model.addAttribute("studentsService", studentDAO);
        return "students";
    }

    @GetMapping("/add_student")
    public String addStudent(Model model) {
        Students student = new Students();
        student.setId((long) -1);
        model.addAttribute("student", student);
        model.addAttribute("studentsService", studentDAO);
        return "edit_student";
    }

    @GetMapping("/student")
    public String studentPage(@RequestParam(name = "id") Long id,
                              @RequestParam(name = "from") @Nullable Date from,
                              @RequestParam(name = "to") @Nullable Date to,
                              Model model) {
        Students student = studentDAO.getById(id);
        if (student == null) {
            model.addAttribute("error_msg", "Нет студента с id=" + id);
            return "error_page";
        }
        model.addAttribute("student", student);
        model.addAttribute("studentsService", studentDAO);

        List<Courses> courses = new ArrayList<Courses>(studentDAO.getCourses(student.getId()));
        Collections.sort(courses);
        model.addAttribute("courses", courses);

        List<Courses> all_courses = courseDAO.getAll();
        for (Courses course : courses) {
            all_courses.removeIf(e -> Objects.equals(e.getId(), course.getId()));
        }
        Collections.sort(all_courses);
        model.addAttribute("all_courses", all_courses);

        StudentCourse sc = new StudentCourse(student.getId(), null);
        model.addAttribute("student_course", sc);


        List<Lectures> lectures = studentDAO.getLectures(id);
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

        return "student";
    }

    @PostMapping("/student")
    public String studentPagePost(Long id, TimeInterval ti,
                                  Model model) {
        return studentPage(id, ti.getFrom(), ti.getTo(), model);
    }

    @GetMapping("/edit_student")
    public String editStudentPage(@RequestParam(name = "id") Long id, Model model) {
        Students student = studentDAO.getById(id);
        if (student == null) {
            model.addAttribute("error_msg", "Нет студента с id=" + id);
            return "error_page";
        }
        model.addAttribute("student", student);
        model.addAttribute("studentsService", studentDAO);
        return "edit_student";
    }

    @PostMapping("/save_student")
    public String saveStudent(Students in,
                              Model model) {
        if (in.getId() == -1) {
            in.setId(studentDAO.getMaxId() + 1);
            studentDAO.insert(in);
            return "redirect:/students";
        }
        Students student = studentDAO.getById(in.getId());
        if (student != null) {
            studentDAO.update(in.getId(), in);
            return "redirect:/students";
        }

        model.addAttribute("error_msg", "Невозможно сохранить данные");
        return "error_page";
    }

    @PostMapping("/student_add_course")
    public String studentAddCourse(StudentCourse in,
                                   Model model) {
        Students student = studentDAO.getById(in.getStudent_id());
        if (student == null) {
            model.addAttribute("error_msg", "Невозможно сохранить данные");
            return "error_page";
        }
        Courses course = courseDAO.getById(in.getCourse_id());
        if (course == null) {
            model.addAttribute("error_msg", "Невозможно сохранить данные");
            return "error_page";
        }
        studentDAO.addCourse(in.getStudent_id(), in.getCourse_id());
        return "redirect:/student?id=" + in.getStudent_id().toString();
    }

    @PostMapping("/remove_student_from_course")
    public String studentRemoveCourse(@RequestParam(name = "student_id") Long student_id,
                                      @RequestParam(name = "course_id") Long course_id,
                                      Model model) {
        Students student = studentDAO.getById(student_id);
        if (student == null) {
            model.addAttribute("error_msg", "Невозможно сохранить данные");
            return "error_page";
        }
        Courses course = courseDAO.getById(course_id);
        if (course == null) {
            model.addAttribute("error_msg", "Невозможно сохранить данные");
            return "error_page";
        }
        studentDAO.deleteCourse(student_id, course_id);
        return "redirect:/student?id=" + student_id.toString();
    }

    @PostMapping("/remove_student")
    public String removeStudent(@RequestParam(name = "id") Long id,
                                Model model) {
        studentDAO.delete(id);
        return "redirect:/students";
    }

    @GetMapping("/remove_student")
    public String removeStudentGet(@RequestParam(name = "id") Long id,
                                     Model model) {
        return removeStudent(id, model);
    }
}
