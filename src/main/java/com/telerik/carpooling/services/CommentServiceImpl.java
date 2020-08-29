package com.telerik.carpooling.services;

import com.telerik.carpooling.enums.UserRole;
import com.telerik.carpooling.exceptions.MyNotFoundException;
import com.telerik.carpooling.models.Comment;
import com.telerik.carpooling.models.Trip;
import com.telerik.carpooling.models.User;
import com.telerik.carpooling.models.dtos.CommentDtoEdit;
import com.telerik.carpooling.models.dtos.CommentDtoResponse;
import com.telerik.carpooling.models.dtos.dtos.mapper.DtoMapper;
import com.telerik.carpooling.repositories.CommentRepository;
import com.telerik.carpooling.repositories.TripRepository;
import com.telerik.carpooling.repositories.UserRepository;
import com.telerik.carpooling.services.services.contracts.CommentService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;

@RequiredArgsConstructor
@Service
@Log4j2
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final TripRepository tripRepository;
    private final UserRepository userRepository;
    private final DtoMapper dtoMapper;

    @Override
    public CommentDtoResponse createComment(Long tripID,
                                            String loggedUserUsername, String message) throws MyNotFoundException {

        User user = findUserByUsername(loggedUserUsername);
        Comment comment = new Comment();
        comment.setAuthor(user);
        comment.setMessage(message);
        comment.setIsDeleted(false);

        Trip trip = getTripById(tripID);
        comment.setTrip(trip);

        return dtoMapper.objectToDto(commentRepository.save(comment));
    }

    @Override
    public Set<CommentDtoResponse> getComments(Long tripId) throws MyNotFoundException {

        Trip trip = getTripById(tripId);

        return dtoMapper.commentsToCommentsDtoResponses(commentRepository.getAllByTripAndIsDeletedFalse(trip));
    }

    @Override
    public void deleteComment(Long id, String username) throws MyNotFoundException {

        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new MyNotFoundException("Comment with this id not found"));
        User commentAuthor = comment.getAuthor();
        User loggedUser = findUserByUsername(username);

        if (isRole_AdminOrSameUser(commentAuthor, loggedUser)) {
            comment.setIsDeleted(true);
            commentRepository.save(comment);
        } else throw new IllegalArgumentException("You are not authorized to delete the comment");
    }

    @Override
    public CommentDtoResponse updateComment(CommentDtoEdit commentDtoEdit, String username) {
        Comment comment = dtoMapper.dtoToObject(commentDtoEdit);
        User commentAuthor = comment.getAuthor();
        User loggedUser = findUserByUsername(username);
        if (isRole_AdminOrSameUser(commentAuthor, loggedUser)) {

            return dtoMapper.objectToDto(commentRepository.save(comment));
        } else throw new IllegalArgumentException("You are not authorized to edit the comment");
    }


    private User findUserByUsername(String username) {
        return userRepository.findByUsernameAndIsDeletedFalse(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username is not recognized"));
    }

    private Trip getTripById(Long tripID) throws MyNotFoundException {
        return tripRepository.findByModelIdAndIsDeletedFalse(tripID)
                .orElseThrow(() -> new MyNotFoundException("Trip does not exist"));
    }

    private boolean isRole_AdminOrSameUser(User user, User loggedUser) {

        return loggedUser.equals(user) || user.getRole().equals(UserRole.ADMIN);
    }
}
