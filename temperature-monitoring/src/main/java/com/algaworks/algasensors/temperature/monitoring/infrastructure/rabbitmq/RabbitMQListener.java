package com.algaworks.algasensors.temperature.monitoring.infrastructure.rabbitmq;

import static com.algaworks.algasensors.temperature.monitoring.infrastructure.rabbitmq.RabbitMQConfig.QUEUE_NAME;

import com.algaworks.algasensors.temperature.monitoring.api.model.TemperatureLogData;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class RabbitMQListener {

  private static final Logger log = LoggerFactory.getLogger(RabbitMQListener.class);

  @RabbitListener(queues = QUEUE_NAME)
  public void handle(@Payload TemperatureLogData temperatureLogData,
      @Headers Map<String, Object> headers) {
    log.info("Received temperature log data: {}", temperatureLogData);
    log.info("Headers: {}", headers);
  }

}
