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
    Car resolve(CarDtoEdit dto, @TargetType Class<Car> type) {
        return dto != null && dto.getModelId() != 0 ? carRepository.findById(dto.getModelId())
                .orElseThrow(()->new IllegalArgumentException("Car not found")) : Car.NOT_FOUND;
    }

    @ObjectFactory
    Comment resolve(CommentDtoEdit dto, @TargetType Class<Comment> type) {
        return dto != null && dto.getModelId() != 0 ? commentRepository.findById(dto.getModelId())
                .orElseThrow(()->new IllegalArgumentException("Comment not found")) : Comment.NOT_FOUND;
    }

    @ObjectFactory
    Trip resolve(TripDtoEdit dto, @TargetType Class<Trip> type) throws IllegalArgumentException {
        return dto != null && dto.getModelId() != 0 ? tripRepository.findById(dto.getModelId())
                .orElseThrow(()->new IllegalArgumentException("Trip not found")) : Trip.NOT_FOUND;
    }

    @ObjectFactory
    User resolve(UserDtoEdit dto, @TargetType Class<User> type) throws IllegalArgumentException {
        return dto != null && dto.getModelId() != 0 ? userRepository.findById(dto.getModelId())
                .orElseThrow(()->new IllegalArgumentException("User not found")) : User.NOT_FOUND;
    }
}
