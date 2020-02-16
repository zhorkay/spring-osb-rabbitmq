package com.swisscom.cloud.servicebroker.persistence.mongodb.serviceprovider;

public class MongoDbProviderSingleton {
    private volatile static MongoDbProviderSingleton uniqueInstance;
    private int port;

    private MongoDbProviderSingleton() {}

    public static MongoDbProviderSingleton getInstance() {
        if (uniqueInstance == null) {
            synchronized (MongoDbProviderSingleton.class) {
                if (uniqueInstance == null) {
                    uniqueInstance = new MongoDbProviderSingleton();
                    uniqueInstance.port = 27037;
                }
            }
        }
        return uniqueInstance;
    }

    public int getPort() {
        return uniqueInstance.port;
    }

    public int nextPort() {
        int current = uniqueInstance.port;
        uniqueInstance.port += 10;
        return current;
    }

}
