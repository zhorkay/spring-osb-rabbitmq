package com.swisscom.cloud.integration.mq;

import com.swisscom.cloud.servicebroker.persistence.mongodb.model.MongoServiceInstance;
import com.swisscom.cloud.servicebroker.persistence.mongodb.repository.MongoServiceInstanceRepository;
import org.springframework.stereotype.Component;

@Component
public class RabbitMQMessageReceiver {

    private final MongoServiceInstanceRepository repository;

    public RabbitMQMessageReceiver(MongoServiceInstanceRepository repository) {
        this.repository = repository;
    }

    public void consumeMessage(MongoServiceInstance message) {

        System.out.println("Consumed <" + message + ">");
    }

}
