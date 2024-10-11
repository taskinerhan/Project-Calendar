package com.project_calendar.Component;

import com.project_calendar.Configure.RabbitMQConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EmailProducer {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void sendEmail(String emailContent) {
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, RabbitMQConfig.ROUTING_KEY, emailContent);
    }
}