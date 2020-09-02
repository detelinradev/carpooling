package com.telerik.carpooling.model;

import com.telerik.carpooling.enums.UserStatus;
import com.telerik.carpooling.model.base.MappedAudibleBase;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.envers.Audited;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Audited
public class TripUserStatus extends MappedAudibleBase {

    @NotNull(message = "Passenger should be a user")
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @NotNull(message = "Passenger should belong to a trip")
    @ManyToOne(fetch = FetchType.LAZY)
    private Trip trip;

    @NotNull(message = "Passenger should have passengers status")
    private UserStatus userStatus;
}
