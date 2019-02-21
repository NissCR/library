package entities;

import javax.persistence.*;

/**
 * Автор книги
 */
@Entity
@Table(name = "AUTHOR")
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "AUTHOR_ID")
    private int id;
    @Column(name = "NAME", length = 50, unique = true)
    private String fullName;

    public Author(String fullName) {
        this.fullName = fullName;
    }

    public Author() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}
