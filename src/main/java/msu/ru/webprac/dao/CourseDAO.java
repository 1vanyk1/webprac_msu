package msu.ru.webprac.dao;

import msu.ru.webprac.db.Courses;
import msu.ru.webprac.db.Lectures;
import msu.ru.webprac.db.Professors;
import msu.ru.webprac.db.Students;

import java.util.List;
import java.util.Set;

public interface CourseDAO extends BasicDAO<Courses, Long> {

    public Set<Students> getStudents(Long id);

    public void addStudent(Long id, Long student_id);

    public void deleteStudent(Long id, Long student_id);

    public List<Lectures> getLectures(Long id);

    public Set<Professors> getProfessors(Long id);
}
