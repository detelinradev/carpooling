package com.telerik.carpooling.services.services.contracts;

import com.telerik.carpooling.models.Comment;
import com.telerik.carpooling.models.User;
import com.telerik.carpooling.models.dtos.CommentDtoRequest;
import com.telerik.carpooling.models.dtos.CommentDtoResponse;
import com.telerik.carpooling.models.dtos.TripDtoResponse;

import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

public interface CommentService {

    CommentDtoResponse createComment( String tripID, User user, String message);

    Set<CommentDtoResponse> getComments(String tripId);
}
