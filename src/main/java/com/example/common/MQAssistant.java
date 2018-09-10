package com.example.common;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MQAssistant {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void send(String key, Object obj) {
        rabbitTemplate.convertAndSend(key, obj);
    }

}
