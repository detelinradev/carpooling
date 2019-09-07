package com.telerik.carpooling.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.telerik.carpooling.models.base.MappedAudibleBase;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Audited
public class Image extends MappedAudibleBase {

    public static Image NOT_FOUND = new Image("No value","No value",new byte[0],
            User.NOT_FOUND, Car.NOT_FOUND);

    @NotNull(message = "Image should have file name")
    @Size(max = 250, message = "File name should not have more than 250 symbols")
    private String fileName;

    @NotNull(message = "Image should have content type")
    @Size(max = 250, message = "Content type should not have more than 250 symbols")
    private String contentType;

    @Lob
    @NotNull(message = "Image should have data")
    private byte[] data;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user", unique = true)
//    @JsonIgnoreProperties("userImage")
    private User user;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "car",unique = true)
//    @JsonIgnoreProperties("carImage")
    private Car car;

    public Image(String fileName, String contentType, byte[] data,User user) {
        this.fileName = fileName;
        this.contentType = contentType;
        this.data = data;
        this.user = user;
    }

    public Image(String fileName, String contentType, byte[] data, Car car) {
        this.fileName = fileName;
        this.contentType = contentType;
        this.data = data;
        this.car = car;
    }
}
