package com.telerik.carpooling.service;

import com.telerik.carpooling.enums.UserStatus;
import com.telerik.carpooling.exception.MyNotFoundException;
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
                              String feedbackString) throws MyNotFoundException {

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

        return dtoMapper.feedbackToFeedbackDtoResponses(feedbackRepository.getAllByUserAndIsDeletedFalse(user));
    }

    private Trip getTripById(Long tripID) throws MyNotFoundException {
        return tripRepository.findByModelIdAndIsDeletedFalse(tripID)
                .orElseThrow(() -> new MyNotFoundException("Trip does not exist"));
    }

    private User findUserByUsername(String username) {
        return userRepository.findByUsernameAndIsDeletedFalse(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username is not recognized"));
    }
}
