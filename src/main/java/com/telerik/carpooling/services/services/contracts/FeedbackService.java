package com.telerik.carpooling.services.services.contracts;

import com.telerik.carpooling.models.Feedback;
import com.telerik.carpooling.models.Rating;
import com.telerik.carpooling.models.User;

public interface FeedbackService {


    Feedback leaveFeedback(String tripID, User user,String receiver, String feedback);
}
