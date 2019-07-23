package com.telerik.carpooling.models.dtos;

import com.telerik.carpooling.models.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@AllArgsConstructor
@Data
public class CommentDtoResponse {

    private int id;

    private User author;

    private String message;
}
