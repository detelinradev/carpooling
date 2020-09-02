package com.telerik.carpooling.model.dto;

import com.telerik.carpooling.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CommentDtoRequest {

    @NotNull(message = "Comment should have author")
    private User author;

    @NotNull(message = "Comment should have message")
    @Size(max = 250, message = "Comment should not have more than 250 symbols")
    private String message;
}
