package msu.ru.webprac.dao;

import msu.ru.webprac.db.Lectures;
import msu.ru.webprac.db.Professors;

import java.util.List;

public interface ProfessorDAO extends BasicDAO<Professors, Long> {
    public List<Lectures> getLecturesForProfessor(Long id);
}
