package acceptance.accesscontrol

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.DirtiesContext
import pl.pja.s13868.news.mono.accesscontrol.domain.AccessControlFacade
import pl.pja.s13868.news.mono.accesscontrol.domain.AccessControlJavaConfig
import spock.lang.Specification
import spock.lang.Stepwise

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
        facade.registerUser(MotherObject.registerUser1())

        then:
        facade.userCount() == 3
        and:
        def dto = facade.user(MotherObject.USER_NAME_1).get()
        dto.userName == MotherObject.USER_NAME_1
        dto.email == MotherObject.USER_EMAIL_1
    }

    def "should reject registration of existing user"() {
        given: "the existing user"
        facade.registerUser(MotherObject.registerUser1())
        assert facade.user(MotherObject.USER_NAME_1).isPresent()

        when: "registering user with same username"
        facade.registerUser(MotherObject.registerUser1())

        then: "system rejects the user"
        thrown(IllegalStateException)
    }

}
