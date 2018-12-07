package com.example.consumers;

import com.example.constants.MQConstants;
import com.example.model.User;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class BacklogConsumer {

    @RabbitHandler
    @RabbitListener(queues = MQConstants.BACKLOG_WARNING_QUEUE)
    public void processWarning(@Payload User user, @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag, Channel channel) throws Exception {
        System.out.println(user.getName());
        System.out.println("warning");
        channel.basicAck(deliveryTag, false);
    }

    @RabbitHandler
    @RabbitListener(queues = MQConstants.BACKLOG_CLOSE_ORDER_QUEUE)
    public void processCloseOrder(@Payload User user, @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag, Channel channel) throws Exception {
        System.out.println(user.getName());
        System.out.println("close order");
        channel.basicAck(deliveryTag, false);
    }
}
