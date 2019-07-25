package com.telerik.carpooling.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.telerik.carpooling.models.base.MappedAudibleBase;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.envers.Audited;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;


@EqualsAndHashCode(callSuper = true, exclude = {"author", "trip"})
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Audited
public class Comment extends MappedAudibleBase {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author", nullable = false)
    @JsonIgnoreProperties("comments")
    private User author;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trip", nullable = false)
    @JsonIgnoreProperties("comments")
    private Trip trip;

    @NotEmpty(message = "Please, write a comment")
    @Size(max = 250, message = "Please, enter comment with not more than 250 symbols")
    private String message;
}
