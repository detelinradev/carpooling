package com.telerik.carpooling.services;

import com.telerik.carpooling.models.Comment;
import com.telerik.carpooling.models.Trip;
import com.telerik.carpooling.models.User;
import com.telerik.carpooling.models.dtos.CommentDtoResponse;
import com.telerik.carpooling.models.dtos.TripDtoResponse;
import com.telerik.carpooling.models.dtos.dtos.mapper.DtoMapper;
import com.telerik.carpooling.repositories.CommentRepository;
import com.telerik.carpooling.repositories.TripRepository;
import com.telerik.carpooling.services.services.contracts.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CommentServiceImpl implements CommentService {

    private final DtoMapper dtoMapper;
    private final CommentRepository commentRepository;
    private final TripRepository tripRepository;

    @Override
    public CommentDtoResponse createComment( TripDtoResponse tripDtoRequest,
                                            User user, String message) {
        Comment comment = new Comment();
        comment.setAuthor(user);
        comment.setMessage(message);

        Optional<Trip> trip =tripRepository.findById(tripDtoRequest.getId()) ;
        trip.ifPresent(value -> value.getComments().add(comment));

        return dtoMapper.objectToDto(commentRepository.save(comment));

    }
}
