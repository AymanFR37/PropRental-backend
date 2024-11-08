package com.backend.proprental.entity;

import com.backend.proprental.entity.utilities.AbstractEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Table(name = "property_imqges")
public class PropertyImage extends AbstractEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "property_id", nullable = false)
    private Property property;

    @Column(name = "image_url", nullable = false, length = 156)
    private String imageUrl;
    @Column(name = "image_description", length = 556)
    private String imageDescription;
    @Column(name = "image_order", nullable = false)
    private Integer imageOrder;
    @Column(name = "is_main_image", nullable = false)
    private Boolean isMainImage;
}
