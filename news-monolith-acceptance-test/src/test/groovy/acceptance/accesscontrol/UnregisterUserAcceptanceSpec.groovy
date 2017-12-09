package acceptance.accesscontrol

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.DirtiesContext
import pl.pja.s13868.news.mono.accesscontrol.domain.AccessControlFacade
import pl.pja.s13868.news.mono.accesscontrol.domain.AccessControlJavaConfig
import pl.pja.s13868.news.mono.accesscontrol.domain.dto.UnregisterUserDto
import pl.pja.s13868.news.mono.accesscontrol.domain.exception.CannotUnregisterUserException
import spock.lang.Specification
import spock.lang.Stepwise

import static acceptance.accesscontrol.MotherObject.*

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@SpringBootTest(classes = [AccessControlJavaConfig], webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Stepwise
class UnregisterUserAcceptanceSpec extends Specification {

    @Autowired
    AccessControlFacade facade

    def "should unregister user"() {
        given: "3 pre-defined users in the system"
        facade.registerUser(registerUser1())
        assert facade.userCount() == 3

        when: "I register a new user"
        facade.unregisterUser(unregisterUser1())

        then: "2 users left in the system"
        facade.userCount() == 2
        and: "the user cannot be found in the system "
        facade.user(USER_NAME_1).isPresent() == false
    }

    def "should reject non-existing user"() {
        given: "one existing user"
        facade.registerUser(registerUser1())
        assert facade.user(USER_NAME_1).isPresent()

        when: "registering a new user with the occupied username"
        facade.unregisterUser(new UnregisterUserDto("non_existing"))

        then: "system rejects the user"
        thrown(CannotUnregisterUserException)
    }

}
