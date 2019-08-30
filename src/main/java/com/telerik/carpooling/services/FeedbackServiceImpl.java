package com.telerik.carpooling.services;

import com.telerik.carpooling.models.Feedback;
import com.telerik.carpooling.models.Trip;
import com.telerik.carpooling.models.User;
import com.telerik.carpooling.models.dtos.FeedbackDtoResponse;
import com.telerik.carpooling.models.dtos.dtos.mapper.DtoMapper;
import com.telerik.carpooling.repositories.FeedbackRepository;
import com.telerik.carpooling.repositories.TripRepository;
import com.telerik.carpooling.repositories.UserRepository;
import com.telerik.carpooling.services.services.contracts.FeedbackService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Log4j2
public class FeedbackServiceImpl implements FeedbackService {

    private final UserRepository userRepository;
    private final TripRepository tripRepository;
    private final FeedbackRepository feedbackRepository;
    private final DtoMapper dtoMapper;

    public Feedback leaveFeedback(String tripID, User user, String receiverString, String feedbackString) {

        long longTripID = parseStringToLong(tripID);
        long longReceiverID = parseStringToLong(receiverString);

        Optional<Trip> trip = tripRepository.findByModelIdAndIsDeleted(longTripID);
        Optional<User> receiver = userRepository.findById(longReceiverID);
        if (trip.isPresent() && receiver.isPresent()) {
            if (trip.get().getPassengerStatus().containsKey(receiver.get())
                    || trip.get().getPassengerStatus().containsKey(user)
                    && (trip.get().getDriver().equals(user) || trip.get().getDriver().equals(receiver.get()))) {
                boolean isDriver = trip.get().getDriver().equals(receiver.get());
                Feedback feedback = new Feedback(user,receiver.get(),feedbackString,isDriver);
                System.out.println(feedback.getFeedback());
                return feedbackRepository.save(feedback);
            }
        }
        return null;
    }

    @Override
    public Set<FeedbackDtoResponse> getFeedback(String userID) {

        long longUserID = parseStringToLong(userID);
        Optional<User> user = userRepository.findById(longUserID);

        return user.map(value -> dtoMapper.feedbackToFeedbackDtoResponses(feedbackRepository.getAllByUser(value))).orElse(null);
    }

    private long parseStringToLong(String tripID) {
        long longTripID = 0;
        try {
            longTripID = Long.parseLong(tripID);
        } catch (NumberFormatException e) {
            log.error("Exception during parsing", e);
        }
        return longTripID;
    }
}
