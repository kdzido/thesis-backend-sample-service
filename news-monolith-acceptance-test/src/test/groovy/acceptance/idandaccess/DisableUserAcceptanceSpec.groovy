package acceptance.idandaccess

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.DirtiesContext
import pl.pja.s13868.news.mono.identityandaccess.domain.IdAndAccessFacade
import pl.pja.s13868.news.mono.identityandaccess.domain.IdAndAccessJavaConfig
import pl.pja.s13868.news.mono.identityandaccess.domain.dto.EnableDisableUserDto
import pl.pja.s13868.news.mono.identityandaccess.domain.dto.RegisterUserDto
import spock.lang.Specification
import spock.lang.Stepwise

@DirtiesContext
@SpringBootTest(classes = [IdAndAccessJavaConfig], webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Stepwise
class DisableUserAcceptanceSpec extends Specification {

    final USER_NAME = "user"
    final USER_EMAIL = "krzysztof.dzido@gmail.com"
    final USER_PASS = "secret"

    @Autowired
    IdAndAccessFacade facade

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

    def "should disable user"() {
        given: "the disabled user present"
        facade.registerUser(registration)
        assert facade.user(USER_NAME).get().isEnabled() == false
        facade.enableUser(new EnableDisableUserDto(USER_NAME, true))

        when:
        facade.enableUser(new EnableDisableUserDto(USER_NAME, false))

        then:
        facade.user(USER_NAME).get().isEnabled()
    }

    def "should reject disabling of non-existing user"() {

        when: "enable user that does not exist"
        facade.enableUser(new EnableDisableUserDto('non-existig-user', false))

        then: "system rejects the enablement"
        thrown(IllegalStateException)
    }

}
