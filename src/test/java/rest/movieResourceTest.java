package rest;

import entities.Movie;
import utils.EMF_Creator;
import io.restassured.RestAssured;
import static io.restassured.RestAssured.given;
import io.restassured.http.ContentType;
import io.restassured.parsing.Parser;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.core.UriBuilder;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.util.HttpStatus;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class movieResourceTest {

    private static final int SERVER_PORT = 7777;
    private static final String SERVER_URL = "http://localhost/api";
    private static List<Movie> movies;

    static final URI BASE_URI = UriBuilder.fromUri(SERVER_URL).port(SERVER_PORT).build();
    private static HttpServer httpServer;
    private static EntityManagerFactory emf;

    static HttpServer startServer() {
        ResourceConfig rc = ResourceConfig.forApplication(new ApplicationConfig());
        return GrizzlyHttpServerFactory.createHttpServer(BASE_URI, rc);
    }

    @BeforeAll
    public static void setUpClass() throws IOException {
        EMF_Creator.startREST_TestWithDB();
        emf = EMF_Creator.createEntityManagerFactoryForTest();

        movies = new ArrayList();

        httpServer = startServer();
        httpServer.start();
        while (!httpServer.isStarted()) {
        }

        //Setup RestAssured
        RestAssured.baseURI = SERVER_URL;
        RestAssured.port = SERVER_PORT;
        RestAssured.defaultParser = Parser.JSON;
    }

    @AfterAll
    public static void closeTestServer() {
        EMF_Creator.endREST_TestWithDB();
        httpServer.shutdownNow();

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
        movies.add(new Movie(1997, "Teletubbies", new String[]{"Tinky Winky", "Dipsy", "Dipsy", "Po"}));
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
    public void testServerIsUp() {
        given()
                .when()
                .get("/test")
                .then()
                .statusCode(200);
    }

    @Test
    public void getMovieCountTest() {
        given()
                .contentType(ContentType.JSON)
                .get("/movie/count")
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("count", equalTo(movies.size()));
    }

    @Test
    public void getAllMovieTest() {
        List<String> titles = new ArrayList();

        for (Movie movie : movies) {
            titles.add(movie.getTitle());
        }

        given()
                .contentType(ContentType.JSON)
                .get("/movie/all")
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("size()", is(movies.size()))
                .and()
                .body("title", hasItems(titles.toArray(new String[0])));
    }

    @Test
    public void getMovieByIdTest() {
        given()
                .contentType(ContentType.JSON)
                .get("/movie/" + movies.get(2).getId())
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("title", equalTo(movies.get(2).getTitle()));
    }

    @Test
    public void resetTest() {
        given()
                .contentType(ContentType.JSON)
                .get("/movie/reset")
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("msg", equalTo("Reset performed"));
    }
}
