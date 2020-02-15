package com.swisscom.cloud.servicebroker.persistence.mongodb.service;

import com.swisscom.cloud.config.AppMQConfiguration;
import com.swisscom.cloud.servicebroker.persistence.mongodb.model.MongoServiceInstance;
import com.swisscom.cloud.servicebroker.persistence.mongodb.repository.MongoServiceInstanceRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.cloud.servicebroker.model.instance.*;
import org.springframework.cloud.servicebroker.service.ServiceInstanceService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Map;

@Service
public class MongoServiceInstanceService implements ServiceInstanceService {

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

        //
        // perform the steps necessary to initiate the asynchronous
        // provisioning of all necessary resources
        //

        this.rabbitTemplate.convertAndSend(AppMQConfiguration.topicExchangeName, "swisscom-routingKey",
                new MongoServiceInstance(serviceInstanceId, serviceDefinitionId, planId, parameters));

        String dashboardUrl = "dashboard_url"; /* construct a dashboard URL */

        return Mono.just(CreateServiceInstanceResponse.builder()
                .dashboardUrl(dashboardUrl)
                .async(true)
                .build());
    }

    @Override
    public Mono<DeleteServiceInstanceResponse> deleteServiceInstance(DeleteServiceInstanceRequest deleteServiceInstanceRequest) {
        return null;
    }

    @Override
    public Mono<GetServiceInstanceResponse> getServiceInstance(GetServiceInstanceRequest request) {
        return null;
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
