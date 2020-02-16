package com.swisscom.cloud.servicebroker.persistence.mongodb.service;

import com.swisscom.cloud.ApplicationManager;
import com.swisscom.cloud.config.MQConfiguration;
import com.swisscom.cloud.servicebroker.persistence.mongodb.model.MongoServiceInstance;
import com.swisscom.cloud.servicebroker.persistence.mongodb.repository.MongoServiceInstanceRepository;
import com.swisscom.cloud.servicebroker.persistence.mongodb.serviceprovider.MongoDbProvider;
import com.swisscom.cloud.servicebroker.persistence.mongodb.serviceprovider.MongoDbProviderSingleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.servicebroker.model.instance.*;
import org.springframework.cloud.servicebroker.service.ServiceInstanceService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Map;

@Service
public class MongoServiceInstanceService implements ServiceInstanceService {

    @Autowired
    private ApplicationManager applicationManager;

    private static final Logger logger = LoggerFactory.getLogger(MongoServiceInstanceService.class);

    @Autowired
    private MongoServiceInstanceRepository repository;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Override
    public Mono<CreateServiceInstanceResponse> createServiceInstance(CreateServiceInstanceRequest request) {
        String serviceInstanceId = request.getServiceInstanceId();
        String serviceDefinitionId = request.getServiceDefinitionId();
        String planId = request.getPlanId();
        Map<String, Object> parameters = request.getParameters();
        parameters.put("fromPort", applicationManager.getPort());
        MongoDbProviderSingleton mongos = MongoDbProviderSingleton.getInstance();
        parameters.put("mongoPort", mongos.nextPort());

        MongoServiceInstance instance = this.repository.findById(serviceInstanceId).orElse(null);
        if (instance == null) {
            instance = new MongoServiceInstance(serviceInstanceId, serviceDefinitionId, planId, parameters);
            this.rabbitTemplate.convertAndSend(MQConfiguration.topicExchangeName, "swisscom.queue.create", instance);
        }
        else {
            logger.error("MongoServiceInstance exists: " + instance.toString());
        }
        String dashboardUrl = "dashboard_url"; /* construct a dashboard URL */

        return Mono.just(CreateServiceInstanceResponse.builder()
                .dashboardUrl(dashboardUrl)
                .async(true)
                .build());
    }

    @Override
    public Mono<DeleteServiceInstanceResponse> deleteServiceInstance(DeleteServiceInstanceRequest request) {
        logger.info("swisscom.queue.delete request with: " + request.getServiceInstanceId());
        MongoServiceInstance instance = repository.findById(request.getServiceInstanceId()).orElse(null);
        if (instance != null) {
            this.rabbitTemplate.convertAndSend(
                    MQConfiguration.topicExchangeName,
                    "swisscom.queue.delete",
                    instance
            );
        }
        else {
            logger.error("swisscom.queue.delete request with: " + request.getServiceInstanceId() + " was not found");
        }


        return Mono.just(DeleteServiceInstanceResponse.builder()
                .async(true)
                .build());
    }

    @Override
    public Mono<GetServiceInstanceResponse> getServiceInstance(GetServiceInstanceRequest request) {
        // Todo: only for test. Implementation is missing
        return Mono.just(GetServiceInstanceResponse.builder().dashboardUrl(MongoDbProvider.getStatus()).build());
    }

    @Override
    public Mono<GetLastServiceOperationResponse> getLastOperation(GetLastServiceOperationRequest request) {
        return null;
    }

    @Override
    public Mono<UpdateServiceInstanceResponse> updateServiceInstance(UpdateServiceInstanceRequest request) {
        return null;
    }
}
