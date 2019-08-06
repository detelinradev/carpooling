package com.telerik.carpooling.models.dtos.dtos.mapper;


import com.telerik.carpooling.models.Comment;
import com.telerik.carpooling.models.dtos.CommentDtoResponse;
import com.telerik.carpooling.repositories.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.mapstruct.ObjectFactory;
import org.mapstruct.TargetType;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CommentMapperResolver {


    private final CommentRepository commentRepository;

    @ObjectFactory
    Comment resolve(CommentDtoResponse dto, @TargetType Class<Comment> type) {
        return dto != null && dto.getModelId() != 0 ? commentRepository.findById(dto.getModelId()).orElse(new Comment()) : new Comment();
    }

}
