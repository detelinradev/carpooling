package com.telerik.carpooling.model;

import com.telerik.carpooling.model.base.MappedAudibleBase;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.OptimisticLockType;
import org.hibernate.annotations.OptimisticLocking;
import org.hibernate.envers.Audited;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


@EqualsAndHashCode(callSuper = true)
@Entity
@OptimisticLocking(type = OptimisticLockType.DIRTY)
@DynamicUpdate
@Data
@AllArgsConstructor
@NoArgsConstructor
@Audited
public class Comment extends MappedAudibleBase {

    public static Comment NOT_FOUND = new Comment(User.NOT_FOUND,Trip.NOT_FOUND,"No value");

    @NotNull(message = "Comment should have author")
    @ManyToOne(fetch = FetchType.LAZY)
    private User author;

    @NotNull(message = "Comment should have author")
    @ManyToOne(fetch = FetchType.LAZY)
    private Trip trip;

    @NotNull(message = "Comment should have message")
    @Size(max = 250, message = "Comment should not have more than 250 symbols")
    private String message;
}
