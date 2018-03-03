package com.kdzdo.thesis.sample.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author krzysztof.dzido@gmail.com
 */
@Component
public class ServiceConfig {

    @Value("${sample.property}")
    String sampleProperty;

    public String getSampleProperty() {
        return sampleProperty;
    }
}
