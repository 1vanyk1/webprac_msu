package msu.ru.webprac.db;

import jakarta.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "professors")
public class Professors implements ListBase<Professors, Long>, Comparable<Professors> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, name = "professor_id")
    private Long id;

    @ManyToOne(cascade = {CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "university_id")
    private Universities university;

    @Column(nullable = false, length=20, name = "professor_name")
    private String name;

    @Column(nullable = false, length=40, name = "professor_surname")
    private String surname;

    @Column(nullable = true, length=30, name = "professor_patronymic")
    private String patronymic;

    @Column(nullable = false, name = "professor_birth_date")
    private Date date;

    public Professors(Long id, Universities university, String name, String surname, String patronymic, Date date) {
        this.id = id;
        this.university = university;
        this.name = name;
        this.surname = surname;
        this.patronymic = patronymic;
        this.date = date;
    }

    public Professors() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void replace(Professors other) {
        this.id = other.id;
        this.university = other.university;
        this.name = other.name;
        this.surname = other.surname;
        this.patronymic = other.patronymic;
        this.date = other.date;
    }

    public Universities getUniversity() {
        return university;
    }

    public void setUniversity(Universities university) {
        this.university = university;
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

    @Override
    public int compareTo(Professors other) {
        int tmp = this.surname.compareTo(other.surname);
        if (tmp != 0)
            return tmp;
        tmp = this.name.compareTo(other.name);
        if (tmp != 0)
            return tmp;
        tmp = this.patronymic.compareTo(other.patronymic);
        if (tmp != 0)
            return tmp;
        tmp = this.date.compareTo(other.date);
        if (tmp != 0)
            return tmp;
        return this.id.compareTo(other.id);
    }

    public String getFullName() {
        return surname + " " + name + " " + patronymic;
    }
}
