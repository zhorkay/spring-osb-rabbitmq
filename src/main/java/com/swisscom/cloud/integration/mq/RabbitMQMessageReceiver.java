package com.swisscom.cloud.integration.mq;

import com.swisscom.cloud.servicebroker.persistence.mongodb.model.MongoServiceInstance;
import com.swisscom.cloud.servicebroker.persistence.mongodb.repository.MongoServiceInstanceRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class RabbitMQMessageReceiver {

    private final MongoServiceInstanceRepository repository;

    public RabbitMQMessageReceiver(MongoServiceInstanceRepository repository) {
        this.repository = repository;
    }

    @RabbitListener(queues = "swisscom-osb-queue-create")
    public void consumeMessageCreate(MongoServiceInstance instance) {

        System.out.println("Consumed <" + instance.toString() + "> for Creation");

        repository.save(instance).block();

    }

    @RabbitListener(queues = "swisscom-osb-queue-delete")
    public void consumeMessageDelete(MongoServiceInstance instance) {

        System.out.println("Consumed <" + instance.toString() + "> for Deletion");
        repository.deleteById(instance.getInstanceId()).block();

    }

}
