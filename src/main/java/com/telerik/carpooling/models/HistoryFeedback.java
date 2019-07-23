package com.telerik.carpooling.models;

import com.telerik.carpooling.models.base.MappedAudibleBase;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class HistoryFeedback extends MappedAudibleBase {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user",nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ratedUser",nullable = false)
    private User ratedUser;

    private int rating;

    private String feedback;

    public HistoryFeedback(User user, User ratedUser, int rating) {
        this.user = user;
        this.ratedUser = ratedUser;
        this.rating = rating;
    }

    public HistoryFeedback(User user, User ratedUser, String feedback) {
        this.user = user;
        this.ratedUser = ratedUser;
        this.feedback = feedback;
    }
}
