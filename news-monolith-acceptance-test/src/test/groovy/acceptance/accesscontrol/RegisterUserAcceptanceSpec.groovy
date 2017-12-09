package acceptance.accesscontrol

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.DirtiesContext
import pl.pja.s13868.news.mono.accesscontrol.domain.AccessControlFacade
import pl.pja.s13868.news.mono.accesscontrol.domain.AccessControlJavaConfig
import spock.lang.Specification
import spock.lang.Stepwise

import static acceptance.accesscontrol.MotherObject.*

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@SpringBootTest(classes = [AccessControlJavaConfig], webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Stepwise
class RegisterUserAcceptanceSpec extends Specification {

    @Autowired
    AccessControlFacade facade

    def "should register user"() {
        given: "the system with with 2 predefined users"
        assert facade.userCount() == 2

        when:
        facade.registerUser(registerUser1())

        then:
        facade.userCount() == 3
        and:
        def dto = facade.user(USER_NAME_1).get()
        dto.userName == USER_NAME_1
        dto.email == USER_EMAIL_1
    }

    def "should reject registration of existing user"() {
        given: "the existing user"
        facade.registerUser(registerUser1())
        assert facade.user(USER_NAME_1).isPresent()

        when: "registering user with same username"
        facade.registerUser(registerUser1())

        then: "system rejects the user"
        thrown(IllegalStateException)
    }

}
