package facades;

import entities.Movie;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

/**
 *
 * @author Nicklas Nielsen
 */
public class MovieFacade {

    private static MovieFacade instance;
    private static EntityManagerFactory emf;

    //Private Constructor to ensure Singleton
    private MovieFacade() {
    }

    /**
     *
     * @param _emf
     * @return an instance of this facade class.
     */
    public static MovieFacade getMovieFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new MovieFacade();
        }
        return instance;
    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public Long getMovieCount() {
        EntityManager em = getEntityManager();

        try {
            Query query = em.createQuery("SELECT COUNT(m) FROM Movie m");
                    
                    
            Long count = (Long) query.getSingleResult();

            return count;
        } finally {
            em.close();
        }
    }

    public Movie getMovieById(Long id) {
        EntityManager em = getEntityManager();

        try {
            Movie movie = em.find(Movie.class, id);

            return movie;
        } finally {
            em.close();
        }
    }

    public List<Movie> getMovieByTitle(String title) {
        EntityManager em = getEntityManager();

        try {
            Query query = em.createNamedQuery("Movie.getByTitle");
            query.setParameter("title", title);

            List<Movie> movieList = query.getResultList();

            return movieList;
        } finally {
            em.close();
        }
    }

    public List<Movie> getAllMovies() {
        EntityManager em = getEntityManager();

        try {
            Query query = em.createNamedQuery("Movie.getAll");

            List<Movie> movies = query.getResultList();

            return movies;
        } finally {
            em.close();
        }
    }

    public void resetDB() {
        EntityManager em = getEntityManager();

        try {
            em.getTransaction().begin();
            em.createNamedQuery("Movie.deleteAllRows").executeUpdate();
            em.persist(new Movie(2008, "Iron Man", new String[]{"Robert Downey Jr.", "Jon Favreau", "Gwyneth Paltrow"}));
            em.persist(new Movie(1997, "Teletubbies", new String[]{"Tinky Winky", "Dipsy", "Dipsy", "Po"}));
            em.persist(new Movie(2013, "Test", new String[]{"Matthew Risch", "Kristoffer Cusick"}));
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }
}
