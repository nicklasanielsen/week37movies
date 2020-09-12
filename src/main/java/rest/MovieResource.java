package rest;

import DTOs.MovieDTO;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import entities.Movie;
import utils.EMF_Creator;
import facades.MovieFacade;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

//Todo Remove or change relevant parts before ACTUAL use
@Path("movie")
public class MovieResource {

    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();

    //An alternative way to get the EntityManagerFactory, whithout having to type the details all over the code
    //EMF = EMF_Creator.createEntityManagerFactory(DbSelector.DEV, Strategy.CREATE);
    private static final MovieFacade FACADE = MovieFacade.getMovieFacade(EMF);
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    @Path("count")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String getMovieCount() {
        long count = FACADE.getMovieCount();
        return "{\"count\":" + count + "}";
    }

    @Path("all")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String getAllMovie() {
        List<Movie> movies = FACADE.getAllMovies();
        List<MovieDTO> movieDTOs = new ArrayList();

        for (Movie movie : movies) {
            movieDTOs.add(new MovieDTO(movie));
        }

        return GSON.toJson(movieDTOs);
    }

    @GET
    @Path("/{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public String getMovieById(@PathParam("id") long id) {
        Movie movie = FACADE.getMovieById(id);
        return GSON.toJson(movie);
    }

    @GET
    @Path("/title/{title}")
    @Produces({MediaType.APPLICATION_JSON})
    public String getMovieByName(@PathParam("title") String title) {
        List<Movie> movies = FACADE.getMovieByTitle(title);
        return GSON.toJson(movies);
    }

    @GET
    @Path("/reset")
    @Produces({MediaType.APPLICATION_JSON})
    public String reset() {
        FACADE.resetDB();
        return "{\"msg\":\"Reset performed\"}";
    }
}
