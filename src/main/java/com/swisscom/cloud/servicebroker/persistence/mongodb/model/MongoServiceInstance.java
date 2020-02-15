package com.swisscom.cloud.servicebroker.persistence.mongodb.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;

@Document
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class MongoServiceInstance {

    @Id
    private String instanceId;
    private String serviceDefinitionId;
    private String planId;
    private Map<String, Object> parameters;
}