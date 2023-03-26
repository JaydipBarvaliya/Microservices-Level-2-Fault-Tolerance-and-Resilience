package io.javabrains.moviecatalogservice.models;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

@Service
public class RatingInfoService {

    private final RestTemplate restTemplate;

    public RatingInfoService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }


    @HystrixCommand(fallbackMethod = "ratingServiceFallbackMethod")
    public UserRating getRatingFromRatingService(@PathVariable("userId") String userId) {
        return restTemplate.getForObject("http://ratings-data-service/ratingsdata/user/" + userId, UserRating.class);
    }
    public UserRating ratingServiceFallbackMethod(@PathVariable("userId") String userId) {

        UserRating userRating = new UserRating();
        userRating.setUserId(userId);
        userRating.setRatings(Arrays.asList(new Rating("0",0)));

        return userRating;
    }


}
