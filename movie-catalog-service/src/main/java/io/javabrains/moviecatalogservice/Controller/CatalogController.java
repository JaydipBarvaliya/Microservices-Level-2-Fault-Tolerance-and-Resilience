package io.javabrains.moviecatalogservice.Controller;

import io.javabrains.moviecatalogservice.models.*;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/catalog")
@EnableCircuitBreaker
public class CatalogController {

    private final RestTemplate restTemplate;
    private final MovieService movieService;
    private final RatingInfoService ratingInfoService;

    public CatalogController(RestTemplate restTemplate, MovieService movieService, RatingInfoService ratingInfoService) {
        this.restTemplate = restTemplate;
        this.movieService = movieService;
        this.ratingInfoService = ratingInfoService;
    }


    @RequestMapping("/{userId}")
    public List<Catalog> getCatalog(@PathVariable("userId") String userId) throws InterruptedException {

        UserRating userRating = ratingInfoService.getRatingFromRatingService(userId);
        return userRating.getRatings().stream()
                .map(rating -> {
                    Movie movie = movieService.getMovieFromMovieService(rating);
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