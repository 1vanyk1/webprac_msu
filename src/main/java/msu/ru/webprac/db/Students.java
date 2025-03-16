package msu.ru.webprac.db;

import jakarta.persistence.*;
import org.hibernate.annotations.Cascade;

import java.sql.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "students")
public class Students implements ListBase<Students, Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, name = "student_id")
    private Long id;

    @Column(nullable = false, length=20, name = "student_name")
    private String name;

    @Column(nullable = false, length=40, name = "student_surname")
    private String surname;

    @Column(nullable = true, length=30, name = "student_patronymic")
    private String patronymic;

    @Column(nullable = false, name = "student_birth_date")
    private Date date;

    @Cascade({
            org.hibernate.annotations.CascadeType.SAVE_UPDATE,
            org.hibernate.annotations.CascadeType.MERGE,
            org.hibernate.annotations.CascadeType.PERSIST
    })
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name="students_courses",
            joinColumns=  @JoinColumn(name="students_student_id", referencedColumnName="student_id"),
            inverseJoinColumns= @JoinColumn(name="course_course_id", referencedColumnName="course_id") )
    private Set<Courses> courses = new HashSet<Courses>();

    public Students(Long id, String name, String surname, String patronymic, Date date) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.patronymic = patronymic;
        this.date = date;
    }

    public Students() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void replace(Students other) {
        this.id = other.id;
        this.name = other.name;
        this.surname = other.surname;
        this.patronymic = other.patronymic;
        this.date = other.date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Set<Courses> getCourses() {
        return courses;
    }

    public void setCourses(Set<Courses> courses) {
        this.courses = courses;
    }
}
