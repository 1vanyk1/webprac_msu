package msu.ru.webprac.db.datas;

import msu.ru.webprac.db.Courses;
import msu.ru.webprac.db.Lectures;
import msu.ru.webprac.db.Professors;

import java.sql.Date;

public class LecturesForm {
    private Long id;
    private Courses course;
    private Professors professor;
    private Date date;
    private String time;
    private Long duration;


    public LecturesForm(Long id, Courses course, Professors professor, Date date, String time, Long duration) {
        this.id = id;
        this.course = course;
        this.professor = professor;
        this.date = date;
        this.time = time;
        this.duration = duration;
    }

    public LecturesForm() {
    }

    public void replace(Lectures other) {
        this.id = other.getId();
        this.course = other.getCourse();
        this.professor = other.getProfessor();
        this.date = other.getDate();
        this.time = other.getTime().toString();
        this.duration = other.getDuration();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Courses getCourse() {
        return course;
    }

    public void setCourse(Courses course) {
        this.course = course;
    }

    public Professors getProfessor() {
        return professor;
    }

    public void setProfessor(Professors professor) {
        this.professor = professor;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }
}
