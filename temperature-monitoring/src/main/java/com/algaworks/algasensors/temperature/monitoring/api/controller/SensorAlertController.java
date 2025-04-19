package com.algaworks.algasensors.temperature.monitoring.api.controller;

import com.algaworks.algasensors.temperature.monitoring.api.model.SensorAlertInput;
import com.algaworks.algasensors.temperature.monitoring.api.model.SensorAlertOutput;
import com.algaworks.algasensors.temperature.monitoring.domain.model.SensorAlert;
import com.algaworks.algasensors.temperature.monitoring.domain.model.SensorId;
import com.algaworks.algasensors.temperature.monitoring.domain.repository.SensorAlertRepository;
import io.hypersistence.tsid.TSID;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/sensors/{sensorId}/alert")
public class SensorAlertController {

  private final SensorAlertRepository sensorAlertRepository;

  public SensorAlertController(SensorAlertRepository sensorAlertRepository) {
    this.sensorAlertRepository = sensorAlertRepository;
  }

  @GetMapping
  public SensorAlertOutput getDetail(@PathVariable TSID sensorId) {
    SensorAlert sensorAlert = findById(sensorId);

    return SensorAlertOutput.builder()
        .id(sensorAlert.getId().getValue())
        .minTemperature(sensorAlert.getMinTemperature())
        .maxTemperature(sensorAlert.getMaxTemperature())
        .build();
  }

  private SensorAlert findById(TSID sensorId) {
    return sensorAlertRepository.findById(new SensorId(sensorId))
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
  }

  private SensorAlert findByIdOrDefault(TSID sensorId) {
    return sensorAlertRepository.findById(new SensorId(sensorId))
        .orElse(SensorAlert.builder()
            .id(new SensorId(sensorId))
            .minTemperature(null)
            .maxTemperature(null)
            .build());
  }

  @PutMapping
  public SensorAlertOutput createOrUpdate(@PathVariable TSID sensorId,
      @RequestBody SensorAlertInput input) {
    SensorAlert sensorAlert = findByIdOrDefault(sensorId);
    sensorAlert.setMinTemperature(input.minTemperature());
    sensorAlert.setMaxTemperature(input.maxTemperature());
    sensorAlertRepository.saveAndFlush(sensorAlert);

    return SensorAlertOutput.builder()
        .id(sensorAlert.getId().getValue())
        .minTemperature(sensorAlert.getMinTemperature())
        .maxTemperature(sensorAlert.getMaxTemperature())
        .build();
  }

  @DeleteMapping
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delete(@PathVariable TSID sensorId) {
    SensorAlert sensorAlert = findById(sensorId);
    sensorAlertRepository.delete(sensorAlert);
  }

}
