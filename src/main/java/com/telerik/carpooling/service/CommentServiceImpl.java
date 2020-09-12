package com.telerik.carpooling.service;

import com.telerik.carpooling.enums.UserRole;
import com.telerik.carpooling.model.Comment;
import com.telerik.carpooling.model.Trip;
import com.telerik.carpooling.model.User;
import com.telerik.carpooling.model.dto.CommentDtoEdit;
import com.telerik.carpooling.model.dto.CommentDtoResponse;
import com.telerik.carpooling.model.dto.dto.mapper.DtoMapper;
import com.telerik.carpooling.repository.CommentRepository;
import com.telerik.carpooling.repository.TripRepository;
import com.telerik.carpooling.repository.UserRepository;
import com.telerik.carpooling.service.service.contract.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@RequiredArgsConstructor
@Service
@Log4j2
@Transactional(readOnly = true)
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final TripRepository tripRepository;
    private final UserRepository userRepository;
    private final DtoMapper dtoMapper;

    @Override
    @Transactional
    public CommentDtoResponse createComment(Long tripID,
                                            String loggedUserUsername, String message) {

        User user = findUserByUsername(loggedUserUsername);
        Trip trip = getTripById(tripID);

        Comment comment = new Comment(user,trip,message);

        return dtoMapper.objectToDto(commentRepository.save(comment));
    }

    @Override
    public Set<CommentDtoResponse> getComments(Long tripId) {

        Trip trip = getTripById(tripId);

        return dtoMapper.commentsToCommentsDtoResponses(commentRepository.getAllByTripAndIsDeletedFalse(trip));
    }

    @Override
    @Transactional
    public void deleteComment(Long id, String username) {

        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Comment with this id not found"));
        User commentAuthor = comment.getAuthor();
        User loggedUser = findUserByUsername(username);

        if (isRole_AdminOrSameUser(commentAuthor, loggedUser)) {
            comment.setIsDeleted(true);
            commentRepository.save(comment);
        } else throw new IllegalArgumentException("You are not authorized to delete the comment");
    }

    @Override
    @Transactional
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
                .orElseThrow(() -> new IllegalArgumentException("Username is not recognized"));
    }

    private Trip getTripById(Long tripID)  {
        return tripRepository.findByModelIdAndIsDeletedFalse(tripID)
                .orElseThrow(() -> new IllegalArgumentException("Trip with this id does not exist"));
    }

    private boolean isRole_AdminOrSameUser(User user, User loggedUser) {

        return loggedUser.equals(user) || user.getRole().equals(UserRole.ADMIN);
    }
}
