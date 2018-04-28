package com.kdzido.thesis.sample

import groovyx.net.http.RESTClient
import spock.lang.Ignore
import spock.lang.Specification
import spock.lang.Stepwise
import spock.lang.Unroll

import java.util.concurrent.TimeUnit

import static org.awaitility.Awaitility.await

/**
 * @author krzysztof.dzido@gmail.com
 */
@Stepwise
class SampleServiceIntegSpec extends Specification {

    final static EUREKASERVICE_URI_1 = System.getenv("EUREKASERVICE_URI_1")
    final static EUREKASERVICE_URI_2 = System.getenv("EUREKASERVICE_URI_2")
    final static SAMPLESERVICE_URI = System.getenv("SAMPLESERVICE_URI")

//    def configServiceClient = new RESTClient("$CONFIGSERVICE_URI")
    def eurekapeer1Client = new RESTClient("$EUREKASERVICE_URI_1").with {
        setHeaders(Accept: 'application/json')
        it
    }
    def eurekapeer2Client = new RESTClient("$EUREKASERVICE_URI_2").with {
        setHeaders(Accept: 'application/json')
        it
    }

    def sampleServiceClient = new RESTClient("$SAMPLESERVICE_URI").with {
        setHeaders(Accept: 'application/json')
        it
    }

    def "that sample service is registered in Eureka peers"() {
        expect:
        await().atMost(2, TimeUnit.MINUTES).until({
            try {
                def resp = eurekapeer1Client.get(path: "/eureka/apps")
                resp.status == 200 &&
                    resp.headers.'Content-Type' == "application/json" &&
                    resp.data.applications.application.any {it.name == "CONFIGSERVICE" }
            } catch (e) {
                return false
            }
        })

        and:
        await().atMost(2, TimeUnit.MINUTES).until({
            try {
                def resp = eurekapeer2Client.get(path: "/eureka/apps")
                resp.status == 200 &&
                        resp.headers.'Content-Type' == "application/json" &&
                        resp.data.applications.application.any {it.name == "SAMPLESERVICE" }
            } catch (e) {
                return false
            }
        })
    }

    def "that sample service returns central config"() {
        expect:
        await().atMost(3, TimeUnit.MINUTES).until({
            try {
                def resp = sampleServiceClient.get(path: "/v1/config/plain")
//                resp.headers.'Content-Type' == "text/plain" &&
                resp.status == 200 &&
                        resp.data == "plain: This is a Git-backed test property for the sampleservice (default)"
//                    resp.data.name == "plain: " &&
//                    resp.data.profiles == ["$serviceProfile"] &&
//                    resp.data.propertySources.any {
//                        it.name == "https://github.com/kdzido/thesis-config/todoservice/todoservice.yml"  &&
//                                it.source.'todo.property' == "This is a Git-backed test property for the todoservice"
//                    }
            } catch (e) {
                return false
            }
        })

//        await().atMost(3, TimeUnit.MINUTES).until({
//            try {
//                def resp = sampleServiceClient.get(path: "/v1/config/cipher")
//                resp.status == 200 &&
//                        resp.data.name == "cipher: password"
//            } catch (e) {
//                return false
//            }
//        })

//        where:
//        serviceName   | serviceProfile
//        "todoservice" | "default"
    }

}
