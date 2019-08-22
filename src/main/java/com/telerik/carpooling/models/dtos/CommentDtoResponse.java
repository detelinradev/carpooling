package com.telerik.carpooling.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;


@NoArgsConstructor
@AllArgsConstructor
@Data
public class CommentDtoResponse {

    private Long modelId;
    private UserDtoResponse author;
    private String message;
}
