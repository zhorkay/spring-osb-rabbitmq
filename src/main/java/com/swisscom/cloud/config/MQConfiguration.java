package com.swisscom.cloud.config;

import com.swisscom.cloud.servicebroker.persistence.mongodb.service.RabbitMQMessageReceiver;
import com.swisscom.cloud.servicebroker.persistence.mongodb.model.MongoServiceInstance;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.support.converter.DefaultClassMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class MQConfiguration {

    public static final String topicExchangeName = "swisscom-osb-exchange";

    public static final String queueNameCreate = "swisscom-osb-queue-create";
    public static final String queueNameDelete = "swisscom-osb-queue-delete";

    @Bean
    Queue queueForCreation() {
        return new Queue(queueNameCreate, false);
    }

    @Bean
    Queue queueForDeletion() {
        return new Queue(queueNameDelete, false);
    }

    @Bean
    TopicExchange createExchange() {
        return new TopicExchange(topicExchangeName);
    }

    @Bean
    Binding bindingCreate(Queue queueForCreation, TopicExchange exchange) {
        return BindingBuilder.bind(queueForCreation).to(exchange).with("swisscom.queue.create");
    }

    @Bean
    Binding bindingDelete(Queue queueForDeletion, TopicExchange exchange) {
        return BindingBuilder.bind(queueForDeletion).to(exchange).with("swisscom.queue.delete");
    }

    @Bean
    SimpleMessageListenerContainer containerCreate(ConnectionFactory connectionFactory,
                                             MessageListenerAdapter listenerAdapterCreate) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(queueNameCreate);
        listenerAdapterCreate.setMessageConverter(jsonConverter());
        container.setMessageListener(listenerAdapterCreate);
        Jackson2JsonMessageConverter jsonConverter = new Jackson2JsonMessageConverter();
        jsonConverter.setClassMapper(classMapper());

        return container;
    }

    @Bean
    SimpleMessageListenerContainer containerDelete(ConnectionFactory connectionFactory,
                                                   MessageListenerAdapter listenerAdapterDelete) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(queueNameDelete);
        listenerAdapterDelete.setMessageConverter(jsonConverter());
        container.setMessageListener(listenerAdapterDelete);
        Jackson2JsonMessageConverter jsonConverter = new Jackson2JsonMessageConverter();
        jsonConverter.setClassMapper(classMapper());

        return container;
    }

    @Bean
    public DefaultClassMapper classMapper() {
        DefaultClassMapper classMapper = new DefaultClassMapper();
        Map<String, Class<?>> idClassMapping = new HashMap<>();
        idClassMapping.put("mongoServiceInstance", MongoServiceInstance.class);
        classMapper.setIdClassMapping(idClassMapping);
        return classMapper;
    }
    @Bean
    MessageListenerAdapter listenerAdapterCreate(RabbitMQMessageReceiver receiver) {
        return new MessageListenerAdapter(receiver, "consumeMessageCreate");
    }

    @Bean
    MessageListenerAdapter listenerAdapterDelete(RabbitMQMessageReceiver receiver) {
        return new MessageListenerAdapter(receiver, "consumeMessageDelete");
    }

    @Bean
    public MessageConverter jsonConverter() {
        return new Jackson2JsonMessageConverter();
    }

}
