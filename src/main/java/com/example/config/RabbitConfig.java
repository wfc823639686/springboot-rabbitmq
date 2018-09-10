package com.example.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig implements RabbitTemplate.ReturnCallback, RabbitTemplate.ConfirmCallback {


    private Logger log = LogManager.getLogger(RabbitConfig.class);

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactoryPlus(
            SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory,
            Jackson2JsonMessageConverter jackson2JsonMessageConverter) {
        rabbitListenerContainerFactory.setMessageConverter(jackson2JsonMessageConverter);
        return rabbitListenerContainerFactory;
    }

    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter(ObjectMapper objectMapper) {
        return new Jackson2JsonMessageConverter(objectMapper);
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, Jackson2JsonMessageConverter jackson2JsonMessageConverter) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setReturnCallback(this);
        template.setConfirmCallback(this);
        template.setMessageConverter(jackson2JsonMessageConverter);
        return template;
    }

    @Override
    public void returnedMessage(Message message, int i, String s, String s1, String s2) {
        log.info("sender return success" + message.toString() + "===" + i + "===" + s1 + "===" + s2);
    }

    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        if (!ack) {
            log.error("HelloSender消息发送失败" + cause + correlationData.toString());
        } else {
            log.info("HelloSender 消息发送成功 ");
        }
    }
}
