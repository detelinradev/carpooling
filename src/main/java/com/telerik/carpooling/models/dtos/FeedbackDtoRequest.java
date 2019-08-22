package com.telerik.carpooling.models.dtos;

import com.telerik.carpooling.models.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class FeedbackDtoRequest {

    private User author;

    @Size(min = 1,max = 250)
    private String feedback;
}
