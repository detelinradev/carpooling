package com.telerik.carpooling.service;

import com.telerik.carpooling.enums.UserStatus;
import com.telerik.carpooling.model.Feedback;
import com.telerik.carpooling.model.Trip;
import com.telerik.carpooling.model.TripUserStatus;
import com.telerik.carpooling.model.User;
import com.telerik.carpooling.model.dto.FeedbackDtoResponse;
import com.telerik.carpooling.model.dto.dto.mapper.DtoMapper;
import com.telerik.carpooling.repository.FeedbackRepository;
import com.telerik.carpooling.repository.TripRepository;
import com.telerik.carpooling.repository.TripUserStatusRepository;
import com.telerik.carpooling.repository.UserRepository;
import com.telerik.carpooling.service.service.contract.FeedbackService;
import com.telerik.carpooling.service.service.contract.RatingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Log4j2
@Transactional(readOnly = true)
public class FeedbackServiceImpl implements FeedbackService {

    private final UserRepository userRepository;
    private final TripRepository tripRepository;
    private final FeedbackRepository feedbackRepository;
    private final TripUserStatusRepository tripUserStatusRepository;
    private final RatingService ratingService;
    private final DtoMapper dtoMapper;

    @Override
    @Transactional
    public void leaveFeedback(Long tripID, String loggedUserUsername, String receiverUsername,
                              String feedbackString) {

        Trip trip = getTripById(tripID);
        User loggedUser = findUserByUsername(loggedUserUsername);
        User receiver = findUserByUsername(receiverUsername);

        List<TripUserStatus> tripUserStatusList =
                tripUserStatusRepository.findCurrentTripUserStatusForAllUsersByTripAndIsDeletedFalse(trip);

        if (ratingService.doLoggedUserAndInteractedUserBothBelongToTripAndOneOfThemIsDriver
                (loggedUser, receiver, tripUserStatusList)) {

            boolean isDriver = tripUserStatusList
                    .stream()
                    .filter(m -> m.getUser().equals(receiver))
                    .anyMatch(k -> k.getUserStatus().equals(UserStatus.DRIVER));

            Feedback feedback = new Feedback(loggedUser, receiver, feedbackString, isDriver);

            feedbackRepository.save(feedback);

        } else throw new IllegalArgumentException("You are not authorized to leave feedback for this user");
    }

    @Override
    public Set<FeedbackDtoResponse> getFeedback(String username) {

        User user = findUserByUsername(username);

        return dtoMapper.feedbackToFeedbackDtoResponses(feedbackRepository.getAllByUserAndIsDeletedFalse(user));
    }

    private Trip getTripById(Long tripID)  {
        return tripRepository.findByModelIdAndIsDeletedFalse(tripID)
                .orElseThrow(() -> new IllegalArgumentException("Trip does not exist"));
    }

    private User findUserByUsername(String username) {
        return userRepository.findByUsernameAndIsDeletedFalse(username)
                .orElseThrow(() -> new IllegalArgumentException("Username is not recognized"));
    }
}
