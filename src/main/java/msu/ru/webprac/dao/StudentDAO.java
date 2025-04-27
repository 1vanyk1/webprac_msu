package msu.ru.webprac.dao;

import msu.ru.webprac.db.Courses;
import msu.ru.webprac.db.Lectures;
import msu.ru.webprac.db.Students;

import java.util.List;
import java.util.Set;

public interface StudentDAO extends BasicDAO<Students, Long> {
    public Set<Courses> getCourses(Long id);

    public void addCourse(Long id, Long course_id);

    public void deleteCourse(Long id, Long course_id);

    public List<Lectures> getLectures(Long id);
}
