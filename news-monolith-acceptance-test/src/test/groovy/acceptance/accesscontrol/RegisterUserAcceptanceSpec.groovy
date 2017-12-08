package acceptance.accesscontrol

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.DirtiesContext
import pl.pja.s13868.news.mono.accesscontrol.domain.AccessControlFacade
import pl.pja.s13868.news.mono.accesscontrol.domain.AccessControlJavaConfig
import pl.pja.s13868.news.mono.accesscontrol.domain.dto.RegisterUserDto
import spock.lang.Specification
import spock.lang.Stepwise

@DirtiesContext
@SpringBootTest(classes = [AccessControlJavaConfig], webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Stepwise
class RegisterUserAcceptanceSpec extends Specification {

    static final String PREDEFINED_USER_NAME = "user"
    static final String PREDEFINED_USER_EMAIL = "krzysztof.dzido@gmail.com"
    static final String PREDEFINED_USER_PASS = "secret"

    @Autowired
    AccessControlFacade facade

    def registration = new RegisterUserDto(
            PREDEFINED_USER_NAME,
            PREDEFINED_USER_PASS,
            "Full",
            "Name",
            "krzysztof.dzido@gmail.com",
            null,
            null,
            "Bio...",
            "Poland",
            "Lublin")

    def "should register user"() {
        given: "the system with with 2 predefined users"
        assert facade.userCount() == 2

        when:
        facade.registerUser(registration)

        then:
        facade.userCount() == 3
        and:
        def dto = facade.user(PREDEFINED_USER_NAME).get()
        dto.userName == PREDEFINED_USER_NAME
        dto.email == PREDEFINED_USER_EMAIL
    }

    def "should reject registration of existing user"() {
        given: "the existing user"
        assert facade.user(PREDEFINED_USER_NAME).isPresent()

        when: "registering user with same username"
        facade.registerUser(registration)

        then: "system rejects the user"
        thrown(IllegalStateException)
    }

}
