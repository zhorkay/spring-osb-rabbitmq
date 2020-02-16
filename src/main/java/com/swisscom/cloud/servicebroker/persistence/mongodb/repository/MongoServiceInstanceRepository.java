package com.swisscom.cloud.servicebroker.persistence.mongodb.repository;

import com.swisscom.cloud.servicebroker.persistence.mongodb.model.MongoServiceInstance;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MongoServiceInstanceRepository extends MongoRepository<MongoServiceInstance, String> {

}
