package io.javabrains.moviecatalogservice.models;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class MovieService {

    private final RestTemplate restTemplate;

    public MovieService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @HystrixCommand(fallbackMethod = "movieServiceFallbackMethod")
    public Movie getMovieFromMovieService(Rating rating) {
        return restTemplate.getForObject("http://movie-info-service/movies/" + rating.getMovieId(), Movie.class);
    }

    public Movie movieServiceFallbackMethod(Rating rating) {
        return new Movie("", "", "Movie Service is down");
    }


}
