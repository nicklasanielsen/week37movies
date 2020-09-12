package DTOs;

import entities.Movie;
import java.util.Objects;

/**
 *
 * @author Nicklas Nielsen
 */
public class MovieDTO {

    private long id;
    private int releaseYear;
    private String title;

    public MovieDTO(Movie m) {
        this.id = m.getId();
        this.releaseYear = m.getReleaseYear();
        this.title = m.getTitle();
    }

    public MovieDTO() {

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
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
        final MovieDTO other = (MovieDTO) obj;
        if (this.id != other.id) {
            return false;
        }
        if (this.releaseYear != other.releaseYear) {
            return false;
        }
        if (!Objects.equals(this.title, other.title)) {
            return false;
        }
        return true;
    }

}
