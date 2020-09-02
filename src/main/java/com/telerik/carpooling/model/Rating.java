package com.telerik.carpooling.model;

import com.telerik.carpooling.model.base.MappedAudibleBase;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Rating extends MappedAudibleBase {

    public static Rating NOT_FOUND =new Rating(User.NOT_FOUND,User.NOT_FOUND,0,true);

    @NotNull(message = "Rating user should not be null")
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @NotNull(message = "Rated user should not be null")
    @ManyToOne(fetch = FetchType.LAZY)
    private User ratedUser;

    @NotNull(message = "Rating should not be null")
    @Range(min=1,max = 5)
    private Integer rating;

    private Boolean isDriver;

}
