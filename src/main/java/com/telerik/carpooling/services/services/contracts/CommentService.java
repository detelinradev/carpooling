package com.telerik.carpooling.services.services.contracts;

import com.telerik.carpooling.models.User;
import com.telerik.carpooling.models.dtos.CommentDtoRequest;
import com.telerik.carpooling.models.dtos.CommentDtoResponse;
import com.telerik.carpooling.models.dtos.TripDtoResponse;

public interface CommentService {

    CommentDtoResponse createComment( TripDtoResponse trip, User user, String message);
}
