package com.telerik.carpooling.services.services.contracts;

import com.telerik.carpooling.models.Feedback;
import com.telerik.carpooling.models.Rating;
import com.telerik.carpooling.models.User;
import com.telerik.carpooling.models.dtos.FeedbackDtoRequest;
import com.telerik.carpooling.models.dtos.FeedbackDtoResponse;

import java.util.Set;

public interface FeedbackService {


    Feedback leaveFeedback(String tripID, User user, String receiver, String feedback);

    Set<FeedbackDtoResponse> getFeedback(String username);
}
