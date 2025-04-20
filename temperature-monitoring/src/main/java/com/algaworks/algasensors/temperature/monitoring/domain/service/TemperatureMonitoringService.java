package com.algaworks.algasensors.temperature.monitoring.domain.service;

import com.algaworks.algasensors.temperature.monitoring.api.model.TemperatureLogData;
import com.algaworks.algasensors.temperature.monitoring.domain.model.SensorId;
import com.algaworks.algasensors.temperature.monitoring.domain.model.SensorMonitoring;
import com.algaworks.algasensors.temperature.monitoring.domain.model.TemperatureLog;
import com.algaworks.algasensors.temperature.monitoring.domain.model.TemperatureLogId;
import com.algaworks.algasensors.temperature.monitoring.domain.repository.SensorMonitoringRepository;
import com.algaworks.algasensors.temperature.monitoring.domain.repository.TemperatureLogRepository;
import jakarta.transaction.Transactional;
import java.time.OffsetDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class TemperatureMonitoringService {

  private static final Logger log = LoggerFactory.getLogger(TemperatureMonitoringService.class);

  private final SensorMonitoringRepository sensorMonitoringRepository;
  private final TemperatureLogRepository temperatureLogRepository;

  public TemperatureMonitoringService(SensorMonitoringRepository sensorMonitoringRepository,
      TemperatureLogRepository temperatureLogRepository) {
    this.sensorMonitoringRepository = sensorMonitoringRepository;
    this.temperatureLogRepository = temperatureLogRepository;
  }

  @Transactional
  public void processTemperatureReading(TemperatureLogData temperatureLogData) {
    if (temperatureLogData.value().equals(10.5)) {
      throw new RuntimeException("Simulated error");
    }

    sensorMonitoringRepository.findById(new SensorId(temperatureLogData.sensorId()))
        .ifPresentOrElse(
            sensor -> handleSensorMonitoring(temperatureLogData, sensor),
            () -> logIgnoredTemperature(temperatureLogData));
  }

  private void handleSensorMonitoring(TemperatureLogData temperatureLogData,
      SensorMonitoring sensor) {
    if (Boolean.TRUE.equals(sensor.getEnabled())) {
      sensor.setLastTemperature(temperatureLogData.value());
      sensor.setUpdatedAt(OffsetDateTime.now());
      sensorMonitoringRepository.save(sensor);

      TemperatureLog temperatureLog = TemperatureLog.builder()
          .id(new TemperatureLogId(temperatureLogData.id()))
          .registeredAt(temperatureLogData.registeredAt())
          .value(temperatureLogData.value())
          .sensorId(new SensorId(temperatureLogData.sensorId()))
          .build();

      temperatureLogRepository.save(temperatureLog);
      log.info("Temperature Updated: SensorId {} Temp {}", temperatureLogData.sensorId(),
          temperatureLogData.value());
    } else {
      logIgnoredTemperature(temperatureLogData);
    }
  }

  private void logIgnoredTemperature(TemperatureLogData temperatureLogData) {
    log.info("Temperature Ignored: SensorId {} Temp {}", temperatureLogData.sensorId(),
        temperatureLogData.value());
  }
}
