package msu.ru.webprac.controllers;

import msu.ru.webprac.dao.CourseDAO;
import msu.ru.webprac.dao.LectureDAO;
import msu.ru.webprac.dao.ProfessorDAO;
import msu.ru.webprac.dao.StudentDAO;
import msu.ru.webprac.dao.impl.CourseDAOimpl;
import msu.ru.webprac.dao.impl.LectureDAOimpl;
import msu.ru.webprac.dao.impl.ProfessorDAOimpl;
import msu.ru.webprac.dao.impl.StudentDAOimpl;
import msu.ru.webprac.db.*;
import msu.ru.webprac.db.Courses;
import msu.ru.webprac.db.datas.LecturesForm;
import msu.ru.webprac.db.datas.StudentCourse;
import org.antlr.v4.runtime.misc.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Controller
public class CoursesController {
    @Autowired
    private final CourseDAO courseDAO = new CourseDAOimpl();

    @Autowired
    private final StudentDAO studentDAO = new StudentDAOimpl();

    @Autowired
    private final ProfessorDAO professorDAO = new ProfessorDAOimpl();

    @Autowired
    private final LectureDAO lectureDAO = new LectureDAOimpl();

    @GetMapping("/courses")
    public String coursesPage(Model model) {
        List<Courses> courses = (List<Courses>) courseDAO.getAll();
        Collections.sort(courses);
        model.addAttribute("courses", courses);
        model.addAttribute("coursesService", courseDAO);

        return "courses";
    }

    @GetMapping("/add_course")
    public String addCourse(Model model) {
        Courses course = new Courses();
        course.setId((long) -1);
        model.addAttribute("course", course);
        model.addAttribute("coursesService", courseDAO);
        return "add_course";
    }

    @GetMapping("/course")
    public String coursePage(@RequestParam(name = "id") Long id, Model model) {
        Courses course = courseDAO.getById(id);
        if (course == null) {
            model.addAttribute("error_msg", "Нет студента с id=" + id);
            return "error_page";
        }
        model.addAttribute("course", course);
        model.addAttribute("coursesService", courseDAO);

        List<Students> students = new ArrayList<Students>(courseDAO.getStudents(course.getId()));
        Collections.sort(students);
        model.addAttribute("students", students);

        List<Professors> professors = new ArrayList<Professors>(courseDAO.getProfessors(course.getId()));
        Collections.sort(professors);
        model.addAttribute("professors", professors);

        List<Lectures> lectures = courseDAO.getLectures(course.getId());
        Collections.sort(lectures);
        model.addAttribute("lectures", lectures);

        return "course";
    }

    @GetMapping("/edit_course")
    public String editCoursePage(@RequestParam(name = "id") Long id, Model model) {
        Courses course = courseDAO.getById(id);
        if (course == null) {
            model.addAttribute("error_msg", "Нет курса с id=" + id);
            return "error_page";
        }
        model.addAttribute("course", course);
        model.addAttribute("coursesService", courseDAO);

        List<Students> students = new ArrayList<Students>(courseDAO.getStudents(course.getId()));
        Collections.sort(students);
        model.addAttribute("students", students);

        List<Students> all_students = studentDAO.getAll();
        for (Students student : students) {
            all_students.removeIf(e -> Objects.equals(e.getId(), student.getId()));
        }
        Collections.sort(all_students);
        model.addAttribute("all_students", all_students);

        StudentCourse sc = new StudentCourse(null, course.getId());
        model.addAttribute("student_course", sc);

        List<Professors> professors = new ArrayList<Professors>(courseDAO.getProfessors(course.getId()));
        Collections.sort(professors);
        model.addAttribute("professors", professors);

        List<Professors> all_professors = professorDAO.getAll();
        model.addAttribute("all_professors", all_professors);

        List<Lectures> lectures = courseDAO.getLectures(course.getId());
        Collections.sort(lectures);
        model.addAttribute("lectures", lectures);

        return "edit_course";
    }

    @PostMapping("/save_course")
    public String saveCourse(Courses in,
                                 Model model) {
        if (in.getId() == -1) {
            in.setId(courseDAO.getMaxId() + 1);
            courseDAO.insert(in);
            return "redirect:/courses";
        }
        Courses course = courseDAO.getById(in.getId());
        if (course != null) {
            courseDAO.update(in.getId(), in);
            return "redirect:/courses";
        }

        model.addAttribute("error_msg", "Невозможно сохранить данные");
        return "error_page";
    }



    @PostMapping("/course_add_student")
    public String courseAddStudent(StudentCourse in,
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
        courseDAO.addStudent(in.getCourse_id(), in.getStudent_id());
        return "redirect:/edit_course?id=" + in.getCourse_id().toString();
    }

    @PostMapping("/remove_course_from_student")
    public String courseRemoveStudent(@RequestParam(name = "student_id") Long student_id,
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
        courseDAO.deleteStudent(course_id, student_id);
        return "redirect:/edit_course?id=" + course_id.toString();
    }

    @PostMapping("/remove_course")
    public String removeCourse(@RequestParam(name = "id") Long id,
                                   Model model) {
        courseDAO.delete(id);
        return "redirect:/courses";
    }

    @GetMapping("/remove_course")
    public String removeCourseGet(@RequestParam(name = "id") Long id,
                                  Model model) {
        return removeCourse(id, model);
    }


    @GetMapping("/course_add_lecture")
    public String addLecture(@RequestParam(name = "id") Long id,
                             Model model) {
        LecturesForm lecture = new LecturesForm();
        lecture.setId((long) -1);
        Courses course = courseDAO.getById(id);
        if (course == null) {
            model.addAttribute("error_msg", "Нет курса с id=" + id);
            return "error_page";
        }
        lecture.setCourse(course);
        model.addAttribute("lecture", lecture);
        model.addAttribute("lecturesService", lectureDAO);
        model.addAttribute("course", course);
        List<Professors> professors = professorDAO.getAll();
        model.addAttribute("professors", professors);
        return "course_edit_lecture";
    }

    @GetMapping("/course_edit_lecture")
    public String courseEditLecture(@RequestParam(name = "id") Long id,
                                    @RequestParam(name = "lecture_id") Long lecture_id,
                                    Model model) {
        Lectures lecture = lectureDAO.getById(lecture_id);
        if (lecture == null) {
            model.addAttribute("error_msg", "Нет лекции с id=" + lecture_id);
            return "error_page";
        }
        Courses course = courseDAO.getById(id);
        if (course == null) {
            model.addAttribute("error_msg", "Нет курса с id=" + id);
            return "error_page";
        }
        LecturesForm form = new LecturesForm();
        form.replace(lecture);
        model.addAttribute("lecture", form);
        model.addAttribute("lecturesService", lectureDAO);
        model.addAttribute("course", course);
        List<Professors> professors = professorDAO.getAll();
        model.addAttribute("professors", professors);
        return "course_edit_lecture";
    }

    @PostMapping("/course_save_lecture")
    public String courseSaveLecture(LecturesForm form,
                                    Model model) {
        Lectures in = new Lectures();
        in.replace(form);
        if (in.getId() == -1) {
            in.setId(lectureDAO.getMaxId() + 1);
            lectureDAO.insert(in);
            return "redirect:/edit_course?id=" + in.getCourse().getId().toString();
        }
        Lectures lecture = lectureDAO.getById(in.getId());
        in.setCourse(courseDAO.getById(in.getCourse().getId()));
        in.setProfessor(professorDAO.getById(in.getProfessor().getId()));
        if (lecture != null) {
            lectureDAO.update(in.getId(), in);
            return "redirect:/edit_course?id=" + in.getCourse().getId().toString();
        }

        model.addAttribute("error_msg", "Невозможно сохранить данные");
        return "error_page";
    }

    @PostMapping("/course_remove_lecture")
    public String courseRemoveLecture(@RequestParam(name = "id") Long id,
                                      @RequestParam(name = "lecture_id") Long lecture_id,
                                      Model model) {
        lectureDAO.delete(lecture_id);
        return "redirect:/edit_course?id=" + id;
    }
}
