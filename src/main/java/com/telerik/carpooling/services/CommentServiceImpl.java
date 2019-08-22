package com.telerik.carpooling.services;

import com.telerik.carpooling.models.Comment;
import com.telerik.carpooling.models.Trip;
import com.telerik.carpooling.models.User;
import com.telerik.carpooling.models.dtos.CommentDtoResponse;
import com.telerik.carpooling.models.dtos.dtos.mapper.DtoMapper;
import com.telerik.carpooling.repositories.CommentRepository;
import com.telerik.carpooling.repositories.TripRepository;
import com.telerik.carpooling.services.services.contracts.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@RequiredArgsConstructor
@Service
@Log4j2
public class CommentServiceImpl implements CommentService {

    private final DtoMapper dtoMapper;
    private final CommentRepository commentRepository;
    private final TripRepository tripRepository;

    @Override
    public CommentDtoResponse createComment(String tripID,
                                            User user, String message) {
        long longTripID = getLongTripID(tripID);

        Comment comment = new Comment();
        comment.setAuthor(user);
        comment.setMessage(message);

        Optional<Trip> trip = tripRepository.findByModelIdAndIsDeleted(longTripID);
        if (trip.isPresent()) {
            comment.setTrip(trip.get());

            return dtoMapper.objectToDto(commentRepository.save(comment));
        }
        return null;
    }

    @Override
    public Set<CommentDtoResponse> getComments(String tripId) {
        long longTripID = getLongTripID(tripId);
        Optional<Trip> trip = tripRepository.findByModelIdAndIsDeleted(longTripID);
        return trip.map(value -> dtoMapper.commentsToCommentsDtoResponses(commentRepository.getAllByTrip(value))).orElse(null);
    }

    private long getLongTripID(String tripID) {
        long longTripID = 0;
        try {
            longTripID = Long.parseLong(tripID);
        } catch (NumberFormatException e) {
            log.error("Exception during parsing", e);
        }
        return longTripID;
    }
}
