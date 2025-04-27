package msu.ru.webprac.db;

import jakarta.persistence.*;

@Entity
@Table(name = "universities")
public class Universities implements ListBase<Universities, Long>, Comparable<Universities> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, name = "university_id")
    private Long id;

    @Column(nullable = false, name = "university_name")
    private String name;

    @Column(nullable = false, name = "university_address")
    private String address;

    public Universities(Long id, String name, String address) {
        this.id = id;
        this.name = name;
        this.address = address;
    }

    public Universities() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void replace(Universities other) {
        this.id = other.id;
        this.name = other.name;
        this.address = other.address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public int compareTo(Universities other) {
        int tmp = this.name.compareTo(other.name);
        if (tmp != 0)
            return tmp;
        tmp = this.address.compareTo(other.address);
        if (tmp != 0)
            return tmp;
        return this.id.compareTo(other.id);
    }
}
