package com.telerik.carpooling.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;


@NoArgsConstructor
@AllArgsConstructor
@Data
public class CommentDtoResponse {

    private long id;

    private UserDtoResponse author;

    @Size(min = 1,max = 250)
    private String message;
}
