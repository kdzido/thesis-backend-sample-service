package com.kdzdo.thesis.sample.ports.rest;

import com.kdzdo.thesis.sample.config.ServiceConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author krzysztof.dzido@gmail.com
 */
@RestController
@RequestMapping(value="v1")
class SampleController {

    @Autowired
    ServiceConfig serviceConfig;

    @RequestMapping(value="/config", method = RequestMethod.GET)
    public String getLicenses() {
        return "cloud config value: " + serviceConfig.getSampleProperty();
    }

}
