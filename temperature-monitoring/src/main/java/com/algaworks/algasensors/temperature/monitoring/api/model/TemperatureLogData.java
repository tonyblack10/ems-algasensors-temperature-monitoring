package com.algaworks.algasensors.temperature.monitoring.api.model;

import io.hypersistence.tsid.TSID;

import java.time.OffsetDateTime;
import java.util.UUID;

public record TemperatureLogData(
        UUID id,
        TSID sensorId,
        OffsetDateTime registeredAt,
        Double value
) {
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private UUID id;
        private TSID sensorId;
        private OffsetDateTime registeredAt;
        private Double value;

        public Builder id(UUID id) {
            this.id = id;
            return this;
        }

        public Builder sensorId(TSID sensorId) {
            this.sensorId = sensorId;
            return this;
        }

        public Builder registeredAt(OffsetDateTime registeredAt) {
            this.registeredAt = registeredAt;
            return this;
        }

        public Builder value(Double value) {
            this.value = value;
            return this;
        }

        public TemperatureLogData build() {
            return new TemperatureLogData(id, sensorId, registeredAt, value);
        }
    }


}
