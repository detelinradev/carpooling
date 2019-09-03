package com.telerik.carpooling.services;

import com.telerik.carpooling.models.Comment;
import com.telerik.carpooling.models.Trip;
import com.telerik.carpooling.models.User;
import com.telerik.carpooling.models.dtos.CommentDtoResponse;
import com.telerik.carpooling.models.dtos.dtos.mapper.DtoMapper;
import com.telerik.carpooling.repositories.CommentRepository;
import com.telerik.carpooling.repositories.TripRepository;
import com.telerik.carpooling.repositories.UserRepository;
import com.telerik.carpooling.services.services.contracts.CommentService;
import javassist.NotFoundException;
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
                                            String loggedUserUsername, String message) throws NotFoundException {

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
    public Set<CommentDtoResponse> getComments(Long tripId) throws NotFoundException {
        Trip trip = getTripById(tripId);
        return dtoMapper.commentsToCommentsDtoResponses(commentRepository.getAllByTrip(trip));
    }

    private User findUserByUsername(String username) {
        return userRepository.findByUsernameAndIsDeletedFalse(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username is not recognized"));
    }

    private Trip getTripById(Long tripID) throws NotFoundException {
        return tripRepository.findByModelIdAndIsDeletedFalse(tripID)
                .orElseThrow(() -> new NotFoundException("Trip does not exist"));
    }
}
