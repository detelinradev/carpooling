package com.telerik.carpooling.services;

import com.telerik.carpooling.enums.UserStatus;
import com.telerik.carpooling.models.Feedback;
import com.telerik.carpooling.models.Trip;
import com.telerik.carpooling.models.TripUserStatus;
import com.telerik.carpooling.models.User;
import com.telerik.carpooling.models.dtos.FeedbackDtoResponse;
import com.telerik.carpooling.models.dtos.dtos.mapper.DtoMapper;
import com.telerik.carpooling.repositories.FeedbackRepository;
import com.telerik.carpooling.repositories.TripRepository;
import com.telerik.carpooling.repositories.TripUserStatusRepository;
import com.telerik.carpooling.repositories.UserRepository;
import com.telerik.carpooling.services.services.contracts.FeedbackService;
import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Log4j2
public class FeedbackServiceImpl implements FeedbackService {

    private final UserRepository userRepository;
    private final TripRepository tripRepository;
    private final FeedbackRepository feedbackRepository;
    private final TripUserStatusRepository tripUserStatusRepository;
    private final DtoMapper dtoMapper;

    @Override
    public void leaveFeedback(Long tripID, String loggedUserUsername, String receiverUsername,
                              String feedbackString) throws NotFoundException {

        Trip trip = getTripById(tripID);
        User user = findUserByUsername(loggedUserUsername);
        User receiver = findUserByUsername(receiverUsername);
        List<TripUserStatus> tripUserStatusList = tripUserStatusRepository.findAllByTripAndIsDeletedFalse(trip);
        boolean isDriver = tripUserStatusList.stream().filter(j->j.getUser().equals(receiver))
                .anyMatch(k->k.getUserStatus().equals(UserStatus.DRIVER));

        if (tripUserStatusList.stream().anyMatch(k->k.getUser().equals(receiver))
                && tripUserStatusList.stream().anyMatch(k->k.getUser().equals(user))
                && (tripUserStatusList.stream().filter(j->j.getUser().equals(user))
                .anyMatch(k->k.getUserStatus().equals(UserStatus.DRIVER))
                || isDriver)) {
            Feedback feedback = new Feedback(user, receiver, feedbackString, isDriver);
            feedbackRepository.save(feedback);
        }else throw new IllegalArgumentException("You are not authorized to give feedback to this user");
    }

    @Override
    public Set<FeedbackDtoResponse> getFeedback(String username) {

        User user = findUserByUsername(username);

        return dtoMapper.feedbackToFeedbackDtoResponses(feedbackRepository.getAllByUser(user));
    }

    private Trip getTripById(Long tripID) throws NotFoundException {
        return tripRepository.findByModelIdAndIsDeletedFalse(tripID)
                .orElseThrow(() -> new NotFoundException("Trip does not exist"));
    }

    private User findUserByUsername(String username) {
        return userRepository.findByUsernameAndIsDeletedFalse(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username is not recognized"));
    }
}
