package com.telerik.carpooling.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.telerik.carpooling.models.base.MappedAudibleBase;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.envers.Audited;

import javax.persistence.*;

@EqualsAndHashCode(callSuper = true,exclude = {"user", "car"})
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Audited
public class Image extends MappedAudibleBase {

    @Column(nullable = false)
    private String fileName;


    @Column(nullable = false)
    private String contentType;

    @Lob
    @Column(nullable = false)
    private byte[] data;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user", unique = true)
    @JsonIgnoreProperties("userImage")
    private User user;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "car",unique = true)
    @JsonIgnoreProperties("carImage")
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
