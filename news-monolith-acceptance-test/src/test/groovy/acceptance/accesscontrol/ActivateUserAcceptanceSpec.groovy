package acceptance.accesscontrol

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.DirtiesContext
import pl.pja.s13868.news.mono.accesscontrol.domain.AccessControlFacade
import pl.pja.s13868.news.mono.accesscontrol.domain.AccessControlJavaConfig
import pl.pja.s13868.news.mono.accesscontrol.domain.dto.EnableDisableUserDto
import pl.pja.s13868.news.mono.accesscontrol.domain.dto.RegisterUserDto
import spock.lang.Specification
import spock.lang.Stepwise

@DirtiesContext
@SpringBootTest(classes = [AccessControlJavaConfig], webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Stepwise
class ActivateUserAcceptanceSpec extends Specification {

    final USER_NAME = "user"
    final USER_EMAIL = "krzysztof.dzido@gmail.com"
    final USER_PASS = "secret"

    @Autowired
    AccessControlFacade facade

    def registration = new RegisterUserDto(
            USER_NAME,
            USER_PASS,
            "Full",
            "Name",
            USER_EMAIL,
            null,
            null,
            "Bio...",
            "Poland",
            "Lublin")

    def "should enable user"() {
        given: "non-activated user present"
        def activationHash = facade.registerUser(registration)
        assert facade.user(USER_NAME).get().isActivated() == false

        when:
        facade.activateUser(activationHash)
        then:
        facade.user(USER_NAME).get().isActivated()
    }

    def "should reject activation of already active user"() {
        given: "activated user present"
        def activationHash = facade.registerUser(registration)
        facade.activateUser(activationHash)
        assert facade.user(USER_NAME).get().isActivated()

        when:
        facade.activateUser(activationHash)
        then:
        thrown(IllegalStateException)
    }

    def "should reject activation with invalid hash"() {
        when:
        facade.activateUser("invalid_hash")
        then:
        thrown(IllegalStateException)
    }


}
