package com.telerik.carpooling.services.services.contracts;

import com.telerik.carpooling.models.dtos.FeedbackDtoResponse;
import javassist.NotFoundException;

import java.util.Set;

public interface FeedbackService {


    void leaveFeedback(Long tripID,String loggedUserUsername, String receiverUsername, String feedback) throws NotFoundException;

    Set<FeedbackDtoResponse> getFeedback(String username);
}
