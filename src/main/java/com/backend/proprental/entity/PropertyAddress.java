package com.backend.proprental.entity;

import com.backend.proprental.entity.utilities.AbstractEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Table(name = "property_addresses")
public class PropertyAddress extends AbstractEntity {
    @OneToOne
    @JoinColumn(name = "property_id", nullable = false)
    private Property property;

    @Column(nullable = false)
    private String street;

    @Column(nullable = false)
    private String city;

    private String state;

    @Column(nullable = false)
    private String country;

    private String postalCode;
    private Double latitude;
    private Double longitude;

    @Column(columnDefinition = "TEXT")
    private String directions;
}
