package entities;

import javax.persistence.*;

/**
 * Издательство
 */
@Entity
@Table(name = "PUBLISHER")
public class Publisher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PUBLISHER_ID")
    private int id;
    @Column(name = "NAME", length = 60, unique = true)
    private String name;

    public Publisher(String name) {
        this.name = name;
    }

    public Publisher() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
