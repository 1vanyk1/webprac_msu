package msu.ru.webprac.db;

import jakarta.persistence.*;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.ColumnDefault;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "courses")
public class Courses implements ListBase<Courses, Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, name = "course_id")
    private Long id;

    @Column(nullable = false, name = "course_duration")
    private Long duration;

    @Column(nullable = false, name = "course_name")
    private String name;

    @Column(nullable = true, name = "course_description")
    private String description;

    @Column(nullable = false, name = "course_ended")
    @ColumnDefault("false")
    private Boolean ended;

    @Cascade({
            org.hibernate.annotations.CascadeType.SAVE_UPDATE,
            org.hibernate.annotations.CascadeType.MERGE,
            org.hibernate.annotations.CascadeType.PERSIST
    })
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name="students_courses",
            joinColumns=  @JoinColumn(name="course_course_id", referencedColumnName="course_id"),
            inverseJoinColumns= @JoinColumn(name="students_student_id", referencedColumnName="student_id") )
    private Set<Students> students = new HashSet<Students>();


    public Courses(Long id, Long duration, String name, String description, Boolean ended) {
        this.id = id;
        this.duration = duration;
        this.name = name;
        this.description = description;
        this.ended = ended;
    }

    public Courses() {

    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void replace(Courses other) {
        this.id = other.id;
        this.duration = other.duration;
        this.name = other.name;
        this.description = other.description;
        this.ended = other.ended;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getEnded() {
        return ended;
    }

    public void setEnded(Boolean ended) {
        this.ended = ended;
    }

    public Set<Students> getStudents() {
        return students;
    }

    public void setStudents(Set<Students> students) {
        this.students = students;
    }
}
