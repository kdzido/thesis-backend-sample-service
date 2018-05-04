package com.kdzido.thesis.sample;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

/**
 * @author krzysztof.dzido@gmail.com
 */
@SpringBootApplication
@EnableEurekaClient
@EnableResourceServer
@RefreshScope   // exposes /refresh endpoint to re-read configuration
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
