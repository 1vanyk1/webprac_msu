package msu.ru.webprac.db;

import jakarta.persistence.*;
import java.sql.Time;

@Entity
@Table(name = "lecture")
public class Lectures implements ListBase<Lectures, Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, name = "lecture_id")
    private Long id;

    @ManyToOne(cascade = {CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "course_id")
    private Courses course;

    @ManyToOne(cascade = {CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "professor_id")
    private Professors professor;

    @Column(nullable = false, name = "lecture_day")
    private Long day;

    @Column(nullable = false, name = "lecture_time")
    private Time time;

    @Column(nullable = false, name = "lecture_duration")
    private Long duration;

    public Lectures(Long id, Courses course, Professors professor, Long day, Time time, Long duration) {
        this.id = id;
        this.course = course;
        this.professor = professor;
        this.day = day;
        this.time = time;
        this.duration = duration;
    }

    public Lectures() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void replace(Lectures other) {
        this.id = other.id;
        this.course = other.course;
        this.day = other.day;
        this.time = other.time;
        this.duration = other.duration;
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

    public Long getDay() {
        return day;
    }

    public void setDay(Long day) {
        this.day = day;
    }

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }
}
