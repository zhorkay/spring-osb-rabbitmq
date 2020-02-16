package com.swisscom.cloud;

import org.springframework.boot.web.servlet.context.ServletWebServerInitializedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class ApplicationManager implements ApplicationListener<ServletWebServerInitializedEvent> {
    private int port;

    @Override
    public void onApplicationEvent(ServletWebServerInitializedEvent event) {
        this.port = event.getWebServer().getPort();
    }

    public int getPort() {
        return this.port;
    }
}
