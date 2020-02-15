package com.swisscom.cloud.servicebroker.persistence.mongodb.repository;

import com.swisscom.cloud.servicebroker.persistence.mongodb.model.MongoServiceInstance;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface MongoServiceInstanceRepository extends ReactiveCrudRepository<MongoServiceInstance, String> {

    Flux<MongoServiceInstance> findAllByServiceDefinitionId(String id);

}
