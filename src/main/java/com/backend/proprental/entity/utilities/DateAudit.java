package com.backend.proprental.entity.utilities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@Getter
@Setter
@SuperBuilder
@MappedSuperclass
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(
        value = {"creationDate", "updateDate", "deletionDate"},
        allowGetters = true
)
public abstract class DateAudit implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @CreatedDate
    @Column(name = "creation_date", columnDefinition = "TIMESTAMP WITH TIME ZONE", nullable = false, updatable = false)
    private OffsetDateTime creationDate;

    @LastModifiedDate
    @Column(name = "update_date", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private OffsetDateTime updateDate;

    @Column(name = "deletion_date")
    private LocalDateTime deletionDate;
}
