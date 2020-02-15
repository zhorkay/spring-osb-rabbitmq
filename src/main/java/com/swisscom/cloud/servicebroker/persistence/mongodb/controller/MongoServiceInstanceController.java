package com.swisscom.cloud.servicebroker.persistence.mongodb.controller;

import com.swisscom.cloud.servicebroker.persistence.mongodb.model.MongoServiceInstance;
import com.swisscom.cloud.servicebroker.persistence.mongodb.service.MongoServiceInstanceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.servicebroker.controller.ServiceInstanceController;
import org.springframework.cloud.servicebroker.exception.ServiceBrokerAsyncRequiredException;
import org.springframework.cloud.servicebroker.exception.ServiceDefinitionDoesNotExistException;
import org.springframework.cloud.servicebroker.exception.ServiceInstanceExistsException;
import org.springframework.cloud.servicebroker.model.instance.CreateServiceInstanceRequest;
import org.springframework.cloud.servicebroker.model.instance.CreateServiceInstanceResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@Controller
public class MongoServiceInstanceController {

    public static final String BASE_PATH = "/v3/service_instances";

    private static final Logger logger = LoggerFactory.getLogger(ServiceInstanceController.class);

    private final MongoServiceInstanceService service;

    @Autowired
    public MongoServiceInstanceController(MongoServiceInstanceService service) {
        this.service = service;
    }

    @RequestMapping(value = BASE_PATH + "/{instanceId}", method = RequestMethod.PUT)
    public ResponseEntity<MongoServiceInstance> createServiceInstance(
            @PathVariable("instanceId") String serviceInstanceId,
            @RequestParam(value="accepts_incomplete", required=false) boolean acceptsIncomplete,
            @Valid @RequestBody CreateServiceInstanceRequest request) throws
            ServiceDefinitionDoesNotExistException,
            ServiceInstanceExistsException,
            ServiceBrokerAsyncRequiredException {
        logger.debug("PUT: " + BASE_PATH + "/{instanceId}?accepts_incomplete=" + acceptsIncomplete
                + ", createServiceInstance(), serviceInstanceId = " + serviceInstanceId);

        Mono<CreateServiceInstanceResponse> response = service.createServiceInstance(request);

        logger.debug("Creating a service instance succeeded: serviceInstanceId={}, response={}",
                serviceInstanceId, response);

        return new ResponseEntity<>(HttpStatus.OK);

    }

}
