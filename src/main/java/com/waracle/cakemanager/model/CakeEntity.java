package com.waracle.cakemanager.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity(name = "CAKE")
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class CakeEntity implements Serializable {

    private static final long serialVersionUID = -1798070786993154676L;

    public static final int MAX_TITLE_LENGTH = 100;
    public static final int MAX_DESCRIPTION_LENGTH = 100;
    public static final int MAX_IMAGE_LENGTH = 300;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", unique = true, nullable = false)
    private Integer cakeId;

    @Column(name = "TITLE", unique = true, nullable = false, length = MAX_TITLE_LENGTH)
    @NotNull
    @Length(min = 1, max = MAX_TITLE_LENGTH)
    private String title;

    @Column(name = "DESCRIPTION", unique = false, nullable = false, length = MAX_DESCRIPTION_LENGTH)
    @NotNull
    @Length(min = 1, max = MAX_DESCRIPTION_LENGTH)
    private String description;

    @Column(name = "IMAGE", unique = false, nullable = false, length = MAX_IMAGE_LENGTH)
    @NotNull
    @Length(min = 1, max = MAX_IMAGE_LENGTH)
    @URL
    private String image;

}