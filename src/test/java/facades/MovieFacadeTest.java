package facades;

import utils.EMF_Creator;
import entities.Movie;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class MovieFacadeTest {

    private static EntityManagerFactory emf;
    private static MovieFacade facade;
    private static List<Movie> movies;

    public MovieFacadeTest() {
    }

    @BeforeAll
    public static void setUpClass() {
        emf = EMF_Creator.createEntityManagerFactoryForTest();
        facade = MovieFacade.getMovieFacade(emf);
        movies = new ArrayList();
    }

    @AfterAll
    public static void tearDownClass() {
        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();
            em.createNamedQuery("Movie.deleteAllRows").executeUpdate();
            em.getTransaction().commit();
        } finally {
            em.close();
            emf.close();
        }
    }

    @BeforeEach
    public void setUp() {
        EntityManager em = emf.createEntityManager();

        movies.add(new Movie(2008, "Iron Man", new String[]{"Robert Downey Jr.", "Jon Favreau", "Gwyneth Paltrow"}));
        movies.add(new Movie(1997, "Teletubbies", new String[]{"Tinky Winky", "Dipsy", "Laa-Laa", "Po"}));
        movies.add(new Movie(2013, "Test", new String[]{"Matthew Risch", "Kristoffer Cusick"}));

        try {
            em.getTransaction().begin();
            em.persist(movies.get(0));
            em.persist(movies.get(1));
            em.persist(movies.get(2));
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    @AfterEach
    public void tearDown() {
        EntityManager em = emf.createEntityManager();

        movies.clear();

        try {
            em.getTransaction().begin();
            em.createNamedQuery("Movie.deleteAllRows").executeUpdate();
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    @Test
    public void getMovieCountTest() {
        // Arrange
        Long expected = Integer.toUnsignedLong(movies.size());

        // Act
        Long actual = facade.getMovieCount();

        // Assert
        assertEquals(expected, actual);
    }

    @Test
    public void getMovieByIdTest() {
        // Arrange
        Movie expected = movies.get(1);

        // Act
        Movie actual = facade.getMovieById(expected.getId());

        // Assert
        assertEquals(expected, actual);
    }

    @Test
    public void getMovieByTitleTest() {
        // Arrange
        List<Movie> expected = new ArrayList();
        expected.add(movies.get(0));

        // Act
        List<Movie> actual = facade.getMovieByTitle(movies.get(0).getTitle());

        // Assert
        assertTrue(actual.containsAll(expected));
    }

    @Test
    public void getAllMoviesTest() {
        // Arrange
        List<Movie> expected = movies;

        // Act
        List<Movie> actual = facade.getAllMovies();

        // Assert
        assertTrue(actual.containsAll(expected));
    }
}
