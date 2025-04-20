package com.algaworks.algasensors.temperature.monitoring.infrastructure.rabbitmq;

import static com.algaworks.algasensors.temperature.monitoring.infrastructure.rabbitmq.RabbitMQConfig.QUEUE_ALERTING;
import static com.algaworks.algasensors.temperature.monitoring.infrastructure.rabbitmq.RabbitMQConfig.QUEUE_PROCESS_TEMPERATURE;

import com.algaworks.algasensors.temperature.monitoring.api.model.TemperatureLogData;
import com.algaworks.algasensors.temperature.monitoring.domain.service.SensorAlertService;
import com.algaworks.algasensors.temperature.monitoring.domain.service.TemperatureMonitoringService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class RabbitMQListener {

  private static final Logger log = LoggerFactory.getLogger(RabbitMQListener.class);

  private final TemperatureMonitoringService temperatureMonitoringService;
  private final SensorAlertService sensorAlertService;

  public RabbitMQListener(TemperatureMonitoringService temperatureMonitoringService,
      SensorAlertService sensorAlertService) {
    this.temperatureMonitoringService = temperatureMonitoringService;
    this.sensorAlertService = sensorAlertService;
  }

  @RabbitListener(queues = QUEUE_PROCESS_TEMPERATURE, concurrency = "2-3")
  public void handleProcessingTemperature(@Payload TemperatureLogData temperatureLogData) {
    temperatureMonitoringService.processTemperatureReading(temperatureLogData);
  }

  @RabbitListener(queues = QUEUE_ALERTING, concurrency = "2-3")
  public void handleAlerting(@Payload TemperatureLogData temperatureLogData) {
    log.info("Alerting: SensorId {} Temp {}", temperatureLogData.sensorId(),
        temperatureLogData.value());

    sensorAlertService.handleAlert(temperatureLogData);
  }

}
