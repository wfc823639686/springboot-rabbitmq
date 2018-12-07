package com.example.config;

import com.example.constants.MQConstants;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TopicConfig {

    @Bean
    public Queue backlogWarningQueue() {
        return new Queue(MQConstants.BACKLOG_WARNING_QUEUE);
    }

    @Bean
    public Queue backlogCloseOrderQueue() {
        return new Queue(MQConstants.BACKLOG_CLOSE_ORDER_QUEUE);
    }

    @Bean
    public Exchange backlogExchange() {
        return new TopicExchange("msg.backlog");
    }

    @Bean
    public Binding bindingBacklogWarning(Queue backlogWarningQueue,
                                  Exchange backlogExchange) {
        return BindingBuilder.bind(backlogWarningQueue).to(backlogExchange).with("msg.backlog.warning").noargs();
    }

    @Bean
    public Binding bindingBacklogCloseOrder(Queue backlogCloseOrderQueue,
                                  Exchange backlogExchange) {
        return BindingBuilder.bind(backlogCloseOrderQueue).to(backlogExchange).with("msg.backlog.close_order").noargs();
    }

}
