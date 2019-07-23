package com.telerik.carpooling.services;

import com.telerik.carpooling.models.Comment;
import com.telerik.carpooling.models.Trip;
import com.telerik.carpooling.models.User;
import com.telerik.carpooling.models.dtos.CommentDtoResponse;
import com.telerik.carpooling.models.dtos.TripDtoResponse;
import com.telerik.carpooling.models.dtos.dtos.mapper.DtoMapper;
import com.telerik.carpooling.repositories.CommentRepository;
import com.telerik.carpooling.services.services.contracts.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CommentServiceImpl implements CommentService {

    private final DtoMapper dtoMapper;
    private final CommentRepository commentRepository;

    @Override
    public CommentDtoResponse createComment( TripDtoResponse tripDtoRequest,
                                            User user, String message) {

        Comment comment = new Comment();
        comment.setAuthor(user);
        comment.setMessage(message);

        Trip trip = dtoMapper.dtoToObject(tripDtoRequest);
        trip.getComments().add(comment);

        return dtoMapper.objectToDto(commentRepository.save(comment));

    }
}
