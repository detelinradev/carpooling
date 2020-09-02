package com.telerik.carpooling.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@AllArgsConstructor
@Data
public class CommentDtoResponse {

    private Long modelId;
    private UserDtoResponse author;
    private String message;
}
