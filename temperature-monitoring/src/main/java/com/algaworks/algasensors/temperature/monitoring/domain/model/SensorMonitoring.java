package com.algaworks.algasensors.temperature.monitoring.domain.model;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SensorMonitoring {

    @AttributeOverride(name = "value", column = @Column(name = "id", columnDefinition = "BIGINT"))
    private SensorId id;

    @Column(name = "last_temperature")
    private Double lastTemperature;

    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;

    private Boolean enabled;

}
