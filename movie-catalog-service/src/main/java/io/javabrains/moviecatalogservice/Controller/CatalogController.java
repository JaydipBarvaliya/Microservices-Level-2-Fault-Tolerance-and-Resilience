package io.javabrains.moviecatalogservice.Controller;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import io.javabrains.moviecatalogservice.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/catalog")
@EnableCircuitBreaker
public class CatalogController {

    private final RestTemplate restTemplate;
    private final MovieInfo movieInfo;
    private final RatingInfo ratingInfo;

    public CatalogController(RestTemplate restTemplate, MovieInfo movieInfo, RatingInfo ratingInfo) {
        this.restTemplate = restTemplate;
        this.movieInfo = movieInfo;
        this.ratingInfo = ratingInfo;
    }


    @RequestMapping("/{userId}")
    public List<Catalog> getCatalog(@PathVariable("userId") String userId) throws InterruptedException {

        UserRating userRating = ratingInfo.getRatingFromRatingService(userId);
        return userRating.getRatings().stream()
                .map(rating -> {
                    Movie movie = movieInfo.getMovieFromMovieService(rating);
                    return new Catalog(movie.getName(), movie.getDescription(), rating.getRating());
                })
                .collect(Collectors.toList());
        }
}





/*
Alternative WebClient way
Movie movie = webClientBuilder.build().get().uri("http://localhost:8082/movies/"+ rating.getMovieId())
.retrieve().bodyToMono(Movie.class).block();
*/