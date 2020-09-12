package com.telerik.carpooling.service.service.contract;

import com.telerik.carpooling.exception.MyNotFoundException;
import com.telerik.carpooling.model.dto.CommentDtoEdit;
import com.telerik.carpooling.model.dto.CommentDtoResponse;
import javassist.NotFoundException;

import java.util.Set;

public interface CommentService {

    CommentDtoResponse createComment(Long tripID, String loggedUserUsername, String message) throws NotFoundException, MyNotFoundException;

    Set<CommentDtoResponse> getComments(Long tripId) throws NotFoundException, MyNotFoundException;

    void deleteComment(Long id, String username) throws NotFoundException, MyNotFoundException;

    CommentDtoResponse updateComment(CommentDtoEdit commentDtoEdit, String username);
}
