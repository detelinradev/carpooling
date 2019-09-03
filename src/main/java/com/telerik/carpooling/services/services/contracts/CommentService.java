package com.telerik.carpooling.services.services.contracts;

import com.telerik.carpooling.models.dtos.CommentDtoResponse;
import javassist.NotFoundException;

import java.util.Set;

public interface CommentService {

    CommentDtoResponse createComment(Long tripID, String loggedUserUsername, String message) throws NotFoundException;

    Set<CommentDtoResponse> getComments(Long tripId) throws NotFoundException;
}
