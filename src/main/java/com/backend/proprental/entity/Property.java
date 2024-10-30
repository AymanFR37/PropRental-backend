package com.backend.proprental.entity;

import com.backend.proprental.entity.utilities.AbstractEntity;
import com.backend.proprental.enums.PropertyEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Table(name = "properties")
public class Property extends AbstractEntity {
    @Column(name = "title", nullable = false, length = 156)
    private String title;
    @Column(name = "description", length = 556)
    private String description;
    @Column(name = "address", nullable = false)
    private String address;
    @Column(name = "city", nullable = false)
    private String city;
    @Column(name = "state", nullable = false)
    private String state;
    @Column(name = "zip_code", nullable = false)
    private String zipCode;
    @Column(name = "country", nullable = false)
    private String country;
    @Column(name = "price_per_night", nullable = false)
    private double pricePerNight;
    @Column(name = "max_guests", nullable = false)
    private Integer maxGuests;
    @Enumerated(EnumType.STRING)
    @Column(name = "property_type", nullable = false)
    private PropertyEnum.Type propertyType;
    @Enumerated(EnumType.STRING)
    @Column(name = "property_status", nullable = false)
    private PropertyEnum.Status propertyStatus;
    @ElementCollection
    @Enumerated(EnumType.STRING)
    private List<PropertyEnum.Amenity> propertyAmenities;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;
}
