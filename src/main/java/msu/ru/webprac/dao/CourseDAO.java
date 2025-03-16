package msu.ru.webprac.dao;

import msu.ru.webprac.db.Courses;
import msu.ru.webprac.db.Students;

import java.util.Set;

public interface CourseDAO extends BasicDAO<Courses, Long> {

    public Set<Students> getStudents(Long id);

    public void addStudent(Long id, Long student_id);

    public void deleteStudent(Long id, Long student_id);
}
