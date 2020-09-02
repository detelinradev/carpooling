package com.telerik.carpooling.model;

import com.telerik.carpooling.model.base.MappedAudibleBase;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Feedback extends MappedAudibleBase {

    public static Feedback NOT_FOUND = new Feedback(User.NOT_FOUND,User.NOT_FOUND,"No value",true);

    @NotNull(message = "Feedback should have author")
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @NotNull(message = "Feedback should have author")
    @ManyToOne(fetch = FetchType.LAZY)
    private User receiver;

    @NotNull(message = "Feedback should have content")
    @Size(max = 250, message = "Feedback should not be longer than 250 symbols")
    private String feedback;

    private Boolean isDriver;

}
