package com.telerik.carpooling.models.dtos.dtos.mapper;


import com.telerik.carpooling.models.*;
import com.telerik.carpooling.models.dtos.*;
import com.telerik.carpooling.repositories.*;
import lombok.RequiredArgsConstructor;
import org.mapstruct.ObjectFactory;
import org.mapstruct.TargetType;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MapperResolver {

    private final CarRepository carRepository;
    private final CommentRepository commentRepository;
    private final TripRepository tripRepository;
    private final UserRepository userRepository;
    private final FeedbackRepository feedbackRepository;

    @ObjectFactory
    Car resolve(CarDtoResponse dto, @TargetType Class<Car> type) {
        return dto != null && dto.getModelId() != 0 ? carRepository.findById(dto.getModelId()).orElse(new Car()) : new Car();
    }

    @ObjectFactory
    Comment resolve(CommentDtoResponse dto, @TargetType Class<Comment> type) {
        return dto != null && dto.getModelId() != 0 ? commentRepository.findById(dto.getModelId()).orElse(new Comment()) : new Comment();
    }

    @ObjectFactory
    Feedback resolve(FeedbackDtoResponse dto, @TargetType Class<Comment> type) {
        return dto != null && dto.getModelId() != 0 ? feedbackRepository.findById(dto.getModelId()).orElse(new Feedback()) : new Feedback();
    }

    @ObjectFactory
    Trip resolve(TripDtoEdit dto, @TargetType Class<Trip> type) throws IllegalArgumentException {
        return dto != null && dto.getModelId() != 0 ? tripRepository.findById(dto.getModelId())
                .orElseThrow(()->new IllegalArgumentException("Invalid ID supplied")) : null;
    }

    @ObjectFactory
    User resolve(UserDtoResponse dto, @TargetType Class<User> type) throws IllegalArgumentException {
        return dto != null && dto.getModelId() != 0 ? userRepository.findById(dto.getModelId())
                .orElseThrow(()->new IllegalArgumentException("Invalid ID supplied")) : null;
    }
}
