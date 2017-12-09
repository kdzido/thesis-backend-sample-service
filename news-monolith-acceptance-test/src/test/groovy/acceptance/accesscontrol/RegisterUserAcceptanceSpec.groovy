package acceptance.accesscontrol

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.DirtiesContext
import pl.pja.s13868.news.mono.accesscontrol.domain.AccessControlFacade
import pl.pja.s13868.news.mono.accesscontrol.domain.AccessControlJavaConfig
import pl.pja.s13868.news.mono.accesscontrol.domain.exception.CannotRegisterUserWithOccupiedEmailException
import pl.pja.s13868.news.mono.accesscontrol.domain.exception.CannotRegisterUserWithOccupiedUsernameException
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
        given: "2 pre-defined users in the system"
        assert facade.userCount() == 2

        when: "I register a new user"
        facade.registerUser(registerUser1())

        then: "there are 3 users in the system"
        facade.userCount() == 3
        and: "the new users can be found in the system "
        def dto = facade.user(USER_NAME_1).get()
        dto.userName == USER_NAME_1
        dto.email == USER_EMAIL_1
    }

    def "should reject same username"() {
        given: "one existing user"
        facade.registerUser(registerUser1())
        assert facade.user(USER_NAME_1).isPresent()

        when: "registering a new user with the occupied username"
        facade.registerUser(registerUser2WithSameUsername())

        then: "system rejects the user"
        thrown(CannotRegisterUserWithOccupiedUsernameException)
    }

    def "should reject same email"() {
        given: "1 existing user"
        facade.registerUser(registerUser1())
        assert facade.user(USER_NAME_1).isPresent()

        when: "registering a new user with the occupied email"
        facade.registerUser(registerUser3WithSameEmail())

        then: "system rejects the user"
        thrown(CannotRegisterUserWithOccupiedEmailException)
    }

}
