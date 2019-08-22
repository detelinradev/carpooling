package com.telerik.carpooling.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class FeedbackDtoResponse {

    private Long modelId;
    private UserDtoResponse author;
    private String feedback;
}
