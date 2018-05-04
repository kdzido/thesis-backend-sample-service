package com.kdzido.thesis.sample

import groovyx.net.http.RESTClient
import org.springframework.http.MediaType
import spock.lang.Specification
import spock.lang.Stepwise

import java.util.concurrent.TimeUnit

import static groovyx.net.http.ContentType.URLENC
import static org.awaitility.Awaitility.await

/**
 * @author krzysztof.dzido@gmail.com
 */
@Stepwise
class SampleServiceIntegSpec extends Specification {

    final static EUREKASERVICE_URI_1 = System.getenv("EUREKASERVICE_URI_1")
    final static EUREKASERVICE_URI_2 = System.getenv("EUREKASERVICE_URI_2")
    final static SAMPLESERVICE_URI = System.getenv("SAMPLESERVICE_URI")
    final static AUTHSERVICE_URI = System.getenv("AUTHSERVICE_URI")

    def eurekapeer1Client = new RESTClient("$EUREKASERVICE_URI_1").with {
        setHeaders(Accept: MediaType.APPLICATION_JSON_VALUE)
        it
    }
    def eurekapeer2Client = new RESTClient("$EUREKASERVICE_URI_2").with {
        setHeaders(Accept: MediaType.APPLICATION_JSON_VALUE)
        it
    }
    def authServiceClient = new RESTClient("$AUTHSERVICE_URI").with {
        setHeaders(Accept: MediaType.APPLICATION_JSON_VALUE)
        it
    }

    def "that sample service is registered in Eureka peers"() {
        expect:
        await().atMost(3, TimeUnit.MINUTES).until({
            try {
                def resp = eurekapeer1Client.get(path: "/eureka/apps")
                return resp.status == 200 &&
                    resp.headers.'Content-Type'.contains(MediaType.APPLICATION_JSON_VALUE) &&
                    resp.data.applications.application.any {it.name == "SAMPLESERVICE" }
            } catch (e) {
                return false
            }
        })

        and:
        await().atMost(3, TimeUnit.MINUTES).until({
            try {
                def resp = eurekapeer2Client.get(path: "/eureka/apps")
                return resp.status == 200 &&
                        resp.headers.'Content-Type'.contains(MediaType.APPLICATION_JSON_VALUE) &&
                        resp.data.applications.application.any {it.name == "SAMPLESERVICE" }
            } catch (e) {
                return false
            }
        })
    }

    def "that sample service retrieves central config values"() {
        setup:
        authServiceClient.auth.basic("newsapp", "newsappsecret")
        final passwordGrant = [
                grant_type: "password",
                scope: "mobileclient",
                username: "reader",
                password: "readerpassword"]

        expect:
        await().atMost(4, TimeUnit.MINUTES).until({
            try {
                def authServerResp = authServiceClient.post(
                        path: "/auth/oauth/token",
                        body: passwordGrant,
                        requestContentType : URLENC)    // TODO multipart/form-data

                // token extracted
                final String accessToken = authServerResp.data.'access_token'
                assert accessToken.isEmpty() == false

                def sampleServiceClient = new RESTClient("$SAMPLESERVICE_URI").with {
                    setHeaders(Accept: MediaType.APPLICATION_JSON_VALUE,
                                Authorization: "Bearer $accessToken")
                    it
                }
                def resp = sampleServiceClient.get(path: "/v1/config")
                return resp.status == 200 &&
                        resp.headers.'Content-Type'.contains(MediaType.APPLICATION_JSON_VALUE)
                        resp.data.plain == "This is a Git-backed test property for the sampleservice (default)" &&
                        resp.data.cipher == "password"
            } catch (e) {
                return false
            }
        })
    }

    def "that sample service rejects unauthorized access"() {
        setup:
        authServiceClient.auth.basic("newsapp", "newsappsecret")

        expect:
        await().atMost(4, TimeUnit.MINUTES).until({
            try {
                def sampleServiceClient = new RESTClient("$SAMPLESERVICE_URI").with {
                    setHeaders(Accept: MediaType.APPLICATION_JSON_VALUE,
                            Authorization: "Bearer INVALID_TOKEN")
                    it
                }
                sampleServiceClient.get(path: "/v1/config")
                return false
            } catch (e) {
                assert e.response.status == 401
                return true
            }
        })
    }

}
