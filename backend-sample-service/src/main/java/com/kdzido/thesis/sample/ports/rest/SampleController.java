package com.kdzido.thesis.sample.ports.rest;

import com.google.common.collect.ImmutableMap;
import com.kdzido.thesis.sample.config.ServiceConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author krzysztof.dzido@gmail.com
 */
@RestController
@RequestMapping(value="/v1")
class SampleController {

    @Autowired
    ServiceConfig serviceConfig;

    @RequestMapping(value="/config", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, String> getConfigPlain() {
        return ImmutableMap.of("plain" ,serviceConfig.getSampleProperty(),
                "cipher", serviceConfig.getSamplePassword());
    }

}
