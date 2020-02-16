package com.swisscom.cloud.servicebroker.persistence.mongodb.service;

import com.swisscom.cloud.servicebroker.persistence.mongodb.model.MongoServiceInstance;
import com.swisscom.cloud.servicebroker.persistence.mongodb.repository.MongoServiceInstanceRepository;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.HashMap;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@ContextConfiguration
public class MongoServiceInstanceServiceTest {

    @TestConfiguration
    static class MongoServiceImplTestContextConfiguration {

        @Bean
        public MongoServiceInstanceService mongoServiceInstanceSe() {
            return new MongoServiceInstanceService();
        }
    }

    @MockBean
    private MongoServiceInstanceRepository repository;

    @Before("service test mongo create")
    public void setUp() {
        MongoServiceInstance instance = new MongoServiceInstance("req1", "def1", "plan1", new HashMap<>());

        Mockito.when(repository.findById(instance.getInstanceId()))
                .thenReturn(Optional.of(instance));
    }


}
