package entities;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

/**
 *
 * @author Nicklas Nielsen
 */
@Entity
@NamedQueries({
    @NamedQuery(name = "Movie.deleteAllRows", query = "DELETE from Movie"),
    @NamedQuery(name = "Movie.getAll", query = "SELECT m FROM Movie m"),
    @NamedQuery(name = "Movie.getByTitle", query = "SELECT m FROM Movie m WHERE m.title LIKE CONCAT('%',:title,'%')"),
    @NamedQuery(name = "Movie.getById", query = "SELECT m FROM Movie m WHERE m.id = :id")
})
public class Movie implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int releaseYear;
    private String title;
    private String[] actors;

    public Movie(int releaseYear, String title, String[] actors) {
        this.releaseYear = releaseYear;
        this.title = title;
        this.actors = actors;
    }

    public Movie() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(int releaseYear) {
        this.releaseYear = releaseYear;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String[] getActors() {
        return actors;
    }

    public void setActors(String[] actors) {
        this.actors = actors;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Movie other = (Movie) obj;
        if (this.releaseYear != other.releaseYear) {
            return false;
        }
        if (!Objects.equals(this.title, other.title)) {
            return false;
        }
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Arrays.deepEquals(this.actors, other.actors)) {
            return false;
        }
        return true;
    }

}
