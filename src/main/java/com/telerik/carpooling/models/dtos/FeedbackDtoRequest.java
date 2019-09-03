package com.telerik.carpooling.models.dtos;

import com.telerik.carpooling.models.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class FeedbackDtoRequest {

    @NotNull(message = "Feedback should have author")
    private User author;

    @NotNull(message = "Feedback should have content")
    @Size(max = 250)
    private String feedback;
}
