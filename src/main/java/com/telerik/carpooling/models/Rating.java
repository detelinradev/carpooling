package com.telerik.carpooling.models;

import com.telerik.carpooling.models.base.MappedAudibleBase;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.Size;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Rating extends MappedAudibleBase {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ratingsDriver", nullable = false)
    private User driver;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ratingsPassenger", nullable = false)
    private User passenger;

    @Column(nullable = false)
    @Range(min = 1, max = 5,message = "Please enter rate in range of 1 to 5")
    private int rating;

    @Column(nullable = false)
    private boolean isDriverRating;

    @Size(max=200)
    private String feedback;

}

