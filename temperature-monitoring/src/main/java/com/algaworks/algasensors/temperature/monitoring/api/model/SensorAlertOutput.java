package com.algaworks.algasensors.temperature.monitoring.api.model;

import io.hypersistence.tsid.TSID;

public record SensorAlertOutput(
    TSID id,
    Double maxTemperature,
    Double minTemperature
) {
  // criar builder para o record sem usar o @Builder
  public static SensorAlertOutputBuilder builder() {
    return new SensorAlertOutputBuilder();
  }
  public static class SensorAlertOutputBuilder {
    private TSID id;
    private Double maxTemperature;
    private Double minTemperature;

    public SensorAlertOutputBuilder id(TSID id) {
      this.id = id;
      return this;
    }

    public SensorAlertOutputBuilder maxTemperature(Double maxTemperature) {
      this.maxTemperature = maxTemperature;
      return this;
    }

    public SensorAlertOutputBuilder minTemperature(Double minTemperature) {
      this.minTemperature = minTemperature;
      return this;
    }

    public SensorAlertOutput build() {
      return new SensorAlertOutput(id, maxTemperature, minTemperature);
    }
  }
}
