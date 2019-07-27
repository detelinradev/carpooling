package com.telerik.carpooling.models.dtos.dtos.mapper;

import com.telerik.carpooling.models.User;
import com.telerik.carpooling.models.dtos.UserDtoResponse;
import com.telerik.carpooling.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.mapstruct.ObjectFactory;
import org.mapstruct.TargetType;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMapperResolver {

    private final UserRepository userRepository;

    @ObjectFactory
    User resolve(UserDtoResponse dto, @TargetType Class<User> type) throws IllegalArgumentException {
        return dto != null && dto.getId() != 0 ? userRepository.findById(dto.getId())
                .orElseThrow(()->new IllegalArgumentException("Invalid ID supplied")) : null;
    }
}
