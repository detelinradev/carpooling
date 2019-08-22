package com.telerik.carpooling.services;

import com.telerik.carpooling.enums.UserStatus;
import com.telerik.carpooling.models.Feedback;
import com.telerik.carpooling.models.Trip;
import com.telerik.carpooling.models.User;
import com.telerik.carpooling.repositories.FeedbackRepository;
import com.telerik.carpooling.repositories.TripRepository;
import com.telerik.carpooling.repositories.UserRepository;
import com.telerik.carpooling.services.services.contracts.FeedbackService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Log4j2
public class FeedbackServiceImpl implements FeedbackService {

    private final UserRepository userRepository;
    private final TripRepository tripRepository;
    private final FeedbackRepository feedbackRepository;

    public Feedback leaveFeedback(String tripID, User user, String receiverString, String feedbackString) {

        long intTripID = parseStringToLong(tripID);
        long intReceiverID = parseStringToLong(receiverString);

        Optional<Trip> trip = tripRepository.findByModelIdAndIsDeleted(intTripID);
        Optional<User> receiver = userRepository.findById(intReceiverID);

        if (trip.isPresent() && receiver.isPresent()) {
            if (trip.get().getUserStatus().containsKey(receiver.get()) && trip.get().getUserStatus().containsKey(user)) {
                boolean isDriver = trip.get().getUserStatus().get(receiver.get()).equals(UserStatus.DRIVER);
                Feedback feedback = new Feedback(user,receiver.get(),feedbackString,isDriver);
                return feedbackRepository.save(feedback);
            }
        }
        return null;
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
