package com.example.common;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MQAssistant {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    private Logger log = LogManager.getLogger(MQAssistant.class);

    public void send(String key, Object obj) {
        try {
            rabbitTemplate.convertAndSend(key, obj);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

}
