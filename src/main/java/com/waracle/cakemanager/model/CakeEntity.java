package com.waracle.cakemanager.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity(name = "CAKE")
@Data
public class CakeEntity implements Serializable {

    private static final long serialVersionUID = -1798070786993154676L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", unique = true, nullable = false)
    private Integer cakeId;

    @Column(name = "TITLE", unique = true, nullable = false, length = 100)
    private String title;

    @Column(name = "DESCRIPTION", unique = false, nullable = false, length = 100)
    @JsonProperty("desc")
    private String description;

    @Column(name = "IMAGE", unique = false, nullable = false, length = 300)
    private String image;

}