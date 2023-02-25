package io.javabrains.ratingsdataservice.Controller;

import com.netflix.discovery.converters.Auto;
import io.javabrains.ratingsdataservice.model.Rating;
import io.javabrains.ratingsdataservice.model.UserRating;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ratingsdata")
public class RatingsController {

    @RequestMapping("/movies/{movieId}")
    public Rating getMovieRating(@PathVariable("movieId") String movieId) {
        return new Rating(movieId, 4);
    }

    @RequestMapping("/user/{userId}")
    public UserRating getUserRatings(@PathVariable("userId") String userId) throws InterruptedException {


//        we are sleeping here so parent microservice turn toward fallback mechanism
//        Thread.sleep(4000);

        UserRating userRating = new UserRating();
        userRating.initData(userId);


        return userRating;
    }
}
