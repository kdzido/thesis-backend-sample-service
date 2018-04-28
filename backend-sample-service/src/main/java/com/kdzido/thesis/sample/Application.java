package com.kdzido.thesis.sample;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * @author krzysztof.dzido@gmail.com
 */
@SpringBootApplication
@EnableEurekaClient
@RefreshScope   // exposes /refresh endpoint to re-read configuration
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
