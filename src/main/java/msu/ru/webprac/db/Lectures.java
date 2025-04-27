package msu.ru.webprac.db;

import jakarta.persistence.*;
import msu.ru.webprac.db.datas.LecturesForm;
import org.hibernate.query.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Date;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

@Entity
@Table(name = "lecture")
public class Lectures implements ListBase<Lectures, Long>, Comparable<Lectures> {
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
        this.professor = professor;
        this.day = other.day;
        this.time = other.time;
        this.duration = other.duration;
    }

    public void replace(LecturesForm other) {
        this.id = other.getId();
        this.course = other.getCourse();
        this.professor = other.getProfessor();
        long diff = other.getDate().getTime() - (new Date(125, 0, 0)).getTime();
        this.day = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        long ms;
        try {
            ms = sdf.parse(other.getTime()).getTime();
        } catch (ParseException e) {
            ms = 0;
        }
        this.time = new Time(ms);
        this.duration = other.getDuration();
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

    @Override
    public int compareTo(Lectures other) {
        int tmp = this.day.compareTo(other.day);
        if (tmp != 0)
            return tmp;
        tmp = this.time.compareTo(other.time);
        if (tmp != 0)
            return tmp;
        tmp = this.duration.compareTo(other.duration);
        if (tmp != 0)
            return tmp;
        return this.id.compareTo(other.id);
    }

    public Date getDate() {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date(125, 0, 0));
        c.add(Calendar.DATE, day.intValue());
        return new Date(c.getTime().getTime());
    }

    public Time getEndTime() {
        Calendar c = Calendar.getInstance();
        c.setTime(time);
        c.add(Calendar.MINUTE, duration.intValue());
        return new Time(c.getTime().getTime());
    }

    public String getTimeInterval() {
        return time.toString() + "-" + getEndTime().toString();
    }
}
