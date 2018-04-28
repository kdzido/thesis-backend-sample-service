package com.kdzido.thesis.sample.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author krzysztof.dzido@gmail.com
 */
@Component
public class ServiceConfig {

    @Value("${sample.property}")
    String sampleProperty;

    @Value("${sample.password}")
    String samplePassword;

    public String getSampleProperty() {
        return sampleProperty;
    }

    public String getSamplePassword() {
        return samplePassword;
    }
}
