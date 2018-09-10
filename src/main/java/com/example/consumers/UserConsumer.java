package com.example.consumers;

import com.example.common.MQAssistant;
import com.example.constants.MQConstants;
import com.example.model.User;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class UserConsumer {


//    @RabbitListener(
//            bindings = @QueueBinding(
//                    value = @Queue(value = MQConstants.USER_CREATE_QUEUE, durable = "true"),
//                    exchange = @Exchange(MQConstants.USER_EXCHANGE),
//                    key = MQConstants.USER_CREATE_KEY))
//    public void userCreate(@Payload String str, Channel channel, Message message) {
//        System.out.println(str);
//        mqAssistant.ack(channel, message);
//    }

    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(value = MQConstants.USER_INFO_QUEUE, durable = "true"),
                    exchange = @Exchange(MQConstants.USER_EXCHANGE),
                    key = MQConstants.USER_INFO_KEY))
    public void processUserInfo(@Payload User user, @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag, Channel channel) throws Exception {
        System.out.println(user.getName());
        channel.basicAck(deliveryTag, false);
    }
}