package com.kdzido.thesis.sample.ports.rest;

import com.kdzido.thesis.sample.config.ServiceConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author krzysztof.dzido@gmail.com
 */
@RestController
@RequestMapping(value="/v1/config/")
class SampleController {

    @Autowired
    ServiceConfig serviceConfig;

    @RequestMapping(value="/plain", method = RequestMethod.GET)
    public String getConfigPlain() {
        return "plain: " + serviceConfig.getSampleProperty();
    }

    @RequestMapping(value="/cipher", method = RequestMethod.GET)
    public String getConfigCipher() {
        return "cipher: " + serviceConfig.getSamplePassword();
    }

}
