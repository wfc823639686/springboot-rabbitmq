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

import java.util.Arrays;

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

    // 实现ReturnCallback
    // 当消息发送出去找不到对应路由队列时，将会把消息退回
    // 如果有任何一个路由队列接收投递消息成功，则不会退回消息
    @Override
    public void returnedMessage(Message message, int i, String s, String s1, String s2) {
        log.error("消息发送失败: " + Arrays.toString(message.getBody()));
    }

    // 实现ConfirmCallback
    // ACK=true仅仅标示消息已被Broker接收到，并不表示已成功投放至消息队列中
    // ACK=false标示消息由于Broker处理错误，消息并未处理成功
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        log.info("消息id: " + correlationData + "确认" + (ack ? "成功:" : "失败"));

    }
}
