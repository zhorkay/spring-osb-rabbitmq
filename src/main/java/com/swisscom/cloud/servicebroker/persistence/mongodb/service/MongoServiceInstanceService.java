package com.swisscom.cloud.servicebroker.persistence.mongodb.service;

import com.swisscom.cloud.config.AppMQConfiguration;
import com.swisscom.cloud.servicebroker.persistence.mongodb.model.MongoServiceInstance;
import com.swisscom.cloud.servicebroker.persistence.mongodb.repository.MongoServiceInstanceRepository;
import com.swisscom.cloud.servicebroker.persistence.mongodb.serviceprovider.MongoInstanceManagement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.cloud.servicebroker.model.instance.*;
import org.springframework.cloud.servicebroker.service.ServiceInstanceService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Map;

@Service
public class MongoServiceInstanceService implements ServiceInstanceService {

    private static final Logger logger = LoggerFactory.getLogger(MongoServiceInstanceService.class);

    private final MongoServiceInstanceRepository repository;
    private final RabbitTemplate rabbitTemplate;

    public MongoServiceInstanceService(MongoServiceInstanceRepository repository, RabbitTemplate rabbitTemplate) {
        this.repository = repository;
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public Mono<CreateServiceInstanceResponse> createServiceInstance(CreateServiceInstanceRequest request) {
        String serviceInstanceId = request.getServiceInstanceId();
        String serviceDefinitionId = request.getServiceDefinitionId();
        String planId = request.getPlanId();
        Map<String, Object> parameters = request.getParameters();

        MongoServiceInstance instance = this.repository.findById(serviceInstanceId).block();
        if (instance == null) {
            instance = new MongoServiceInstance(serviceInstanceId, serviceDefinitionId, planId, parameters);
            this.rabbitTemplate.convertAndSend(AppMQConfiguration.topicExchangeName, "swisscom.queue.create", instance);
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
        String serviceInstanceId = request.getServiceInstanceId();
        String serviceDefinitionId = request.getServiceDefinitionId();
        String planId = request.getPlanId();


        MongoServiceInstance instance = this.repository.findById(serviceInstanceId).block();
        if (instance != null) {
            this.rabbitTemplate.convertAndSend(AppMQConfiguration.topicExchangeName, "swisscom.queue.delete", instance);
        }
        else {
            logger.error("MongoServiceInstance does not exist: " + request.toString());
        }

        return Mono.just(DeleteServiceInstanceResponse.builder()
                .async(true)
                .build());
    }

    @Override
    public Mono<GetServiceInstanceResponse> getServiceInstance(GetServiceInstanceRequest request) {
        // Todo: only for test. Implementation is missing
        return Mono.just(GetServiceInstanceResponse.builder().dashboardUrl(MongoInstanceManagement.getStatus()).build());
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
