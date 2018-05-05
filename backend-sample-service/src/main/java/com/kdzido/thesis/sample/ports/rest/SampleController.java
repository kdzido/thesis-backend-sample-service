package com.kdzido.thesis.sample.ports.rest;

import com.google.common.collect.ImmutableMap;
import com.kdzido.thesis.sample.config.ServiceConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.UUID;

/**
 * @author krzysztof.dzido@gmail.com
 */
@RestController
@RequestMapping(value="/v1")
class SampleController {

    static UUID instanceUUID = UUID.randomUUID();

    @Autowired
    ServiceConfig serviceConfig;

    @RequestMapping(value="/config", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, String> getConfigPlain() {
        return ImmutableMap.of("plain" ,serviceConfig.getSampleProperty(),
                "cipher", serviceConfig.getSamplePassword());
    }

    @RequestMapping(value="/delete", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete() {
        // TODO log
    }

    @RequestMapping(value="/uuid", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, String> getInstanceUUID() {
        return ImmutableMap.of("instanceUUID" , String.valueOf(instanceUUID.toString()));
    }

}
