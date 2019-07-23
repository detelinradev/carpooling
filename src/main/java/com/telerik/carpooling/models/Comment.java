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
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Comment extends MappedAudibleBase {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "driver", nullable = false)
    private User author;

    private String message;
}
