package com.telerik.carpooling.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CommentDtoEdit {

    @NotNull(message = "Comment should have id")
    private Long modelId;

    @NotNull(message = "Comment should have message")
    @Size(max = 250, message = "Comment should not have more than 250 symbols")
    private String message;
}
