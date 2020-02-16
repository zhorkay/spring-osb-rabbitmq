package com.swisscom.cloud.servicebroker.persistence.mongodb.service;

import com.swisscom.cloud.ApplicationManager;
import com.swisscom.cloud.servicebroker.persistence.mongodb.model.MongoServiceInstance;
import com.swisscom.cloud.servicebroker.persistence.mongodb.repository.MongoServiceInstanceRepository;
import com.swisscom.cloud.servicebroker.persistence.mongodb.serviceprovider.MongoDbProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RabbitMQMessageReceiver {

    private static final Logger logger = LoggerFactory.getLogger(RabbitMQMessageReceiver.class);

    @Autowired
    private ApplicationManager applicationManager;

    @Autowired
    private MongoServiceInstanceRepository repository;

    @RabbitListener(queues = "swisscom-osb-queue-create")
    public void consumeMessageCreate(MongoServiceInstance instance) throws InterruptedException {
        // TODO: only for demonstration. PLease remove from PROD release
        Thread.sleep(5000);
        logger.info("Consumed with port: " + applicationManager.getPort() + " from producer port: " + instance.getParameters().get("fromPort") + " <" + instance.toString() + "> for Creation");

        // Spin up MongoDb in Docker
        MongoDbProvider.provisionDb(instance);

        // save metadata of Mongo Service instance
        repository.save(instance);

    }

    @RabbitListener(queues = "swisscom-osb-queue-delete")
    public void consumeMessageDelete(MongoServiceInstance instance) {

        logger.info("Consumed with port: " + applicationManager.getPort() + " from producer port: " + instance.getParameters().get("fromPort") + " <" + instance.toString() + "> for Deletion");
        if (repository.findById(instance.getInstanceId()).isPresent()) {
            MongoDbProvider.deProvisionDb(instance);
            repository.deleteById(instance.getInstanceId());
            logger.info("Deleted : " + instance.toString());

        }

    }



}
