package com.telerik.carpooling.service.service.contract;

import com.telerik.carpooling.model.dto.FeedbackDtoResponse;
import javassist.NotFoundException;

import java.util.Set;

public interface FeedbackService {


    void leaveFeedback(Long tripID,String loggedUserUsername, String receiverUsername, String feedback) throws NotFoundException;

    Set<FeedbackDtoResponse> getFeedback(String username);
}
