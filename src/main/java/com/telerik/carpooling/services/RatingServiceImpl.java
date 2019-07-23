package com.telerik.carpooling.services;

import com.telerik.carpooling.models.Rating;
import com.telerik.carpooling.models.User;
import com.telerik.carpooling.repositories.RatingRepository;
import com.telerik.carpooling.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class RatingServiceImpl {

    private final RatingRepository ratingRepository;
    private final UserRepository userRepository;

    void loggingRating(User loggedUser, int ratedUserID, int rating){

        Optional<User> ratedUser = userRepository.findById(ratedUserID);

        if(ratedUser.isPresent()) {
            Rating ratingObject = new Rating(loggedUser, ratedUser.get(), rating);
            ratingRepository.save(ratingObject);
        }
    }
}
