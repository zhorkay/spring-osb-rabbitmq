package com.swisscom.cloud.config;

import com.swisscom.cloud.integration.mq.RabbitMQMessageReceiver;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppMQConfiguration {

    public static final String topicExchangeName = "swisscom-osb-exchange";

    public static final String queueName = "swisscom-osb-queue";

    @Bean
    Queue createQueue() {
        return new Queue(queueName, false);
    }

    @Bean
    TopicExchange createExchange() {
        return new TopicExchange(topicExchangeName);
    }

    @Bean
    Binding bind(Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with("swisscom-routingKey");
    }

    @Bean
    SimpleMessageListenerContainer container(ConnectionFactory connectionFactory,
                                             MessageListenerAdapter listenerAdapter) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(queueName);
        container.setMessageListener(listenerAdapter);
        return container;
    }

    @Bean
    MessageListenerAdapter listenerAdapter(RabbitMQMessageReceiver receiver) {
        return new MessageListenerAdapter(receiver, "consumeMessage");
    }


}
