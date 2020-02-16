package com.swisscom.cloud.servicebroker.persistence.mongodb.serviceprovider;

import com.swisscom.cloud.servicebroker.persistence.mongodb.model.MongoServiceInstance;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MongoDbProvider {

    private static final Logger logger = LoggerFactory.getLogger(MongoDbProvider.class);
    private static final String baseUri = "http://192.168.1.134:5002/mongos";
    public static String getStatus() {

        RestTemplate restTemplate = new RestTemplate();
        String result = restTemplate.getForObject(baseUri, String.class);

        System.out.println(result);

        return result;
    }

    public static String provisionDb(MongoServiceInstance instance) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        Map<String, Object> map = new HashMap<>();
        map.put("instId", instance.getInstanceId());
        map.put("port", instance.getParameters().get("mongoPort"));
        map.put("connection", "");
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(map, headers);

        // send POST request

        ResponseEntity<MongoDb> response = restTemplate.postForEntity(baseUri, entity, MongoDb.class);
        logger.info("created mongodb response: " + Objects.requireNonNull(response.getBody()).toString());
        // check response status code
        if (response.getStatusCode() == HttpStatus.CREATED) {
            return Objects.requireNonNull(response.getBody()).getConnection();
        } else {
            return null;
        }
    }

    public static void deProvisionDb(MongoServiceInstance instance) {
        RestTemplate restTemplate = new RestTemplate();
        Map<String, String> params = new HashMap<>();
        params.put("inst", instance.getInstanceId());
        restTemplate.delete(baseUri + "/{inst}", params);
    }

    @Data
    @ToString
    @AllArgsConstructor
    @NoArgsConstructor
    private static class MongoDb implements Serializable {
        private String instId;
        private int port;
        private String connection;
        private String status;
    }
}
