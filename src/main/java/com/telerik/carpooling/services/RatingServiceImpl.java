package com.telerik.carpooling.services;

import com.telerik.carpooling.models.Rating;
import com.telerik.carpooling.models.User;
import com.telerik.carpooling.repositories.RatingRepository;
import com.telerik.carpooling.repositories.UserRepository;
import com.telerik.carpooling.services.services.contracts.RatingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class RatingServiceImpl implements RatingService {

    private final RatingRepository ratingRepository;

    public void loggingRating(User loggedUser, User ratedUser, int rating) {

        Rating ratingObject = new Rating(loggedUser, ratedUser, rating);
        ratingRepository.save(ratingObject);
    }
}

