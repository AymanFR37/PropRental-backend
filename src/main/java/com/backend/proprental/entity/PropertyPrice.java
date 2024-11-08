package com.backend.proprental.entity;

import com.backend.proprental.entity.utilities.AbstractEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

/*
Benefits of This Approach
Flexible Price Changes: You can easily update or add future prices without modifying existing data. For instance, you could add a new entry for a promotion period or peak season pricing without altering previous records.
Historical Price Tracking: This model allows you to look back at previous pricing periods, which can be useful for analytics or reporting.
Dynamic Pricing Capabilities: By adding new pricing entries, you can introduce various price structures, like weekend pricing, holiday pricing, or different rates for different seasons.
 */

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Table(name = "property_prices")
public class PropertyPrice extends AbstractEntity {
    @ManyToOne
    @JoinColumn(name = "property_id")
    private Property property;
    private Double price;
    private LocalDate startDate; // Date when this price becomes effective
    private LocalDate endDate; // Date when this price expires
}
