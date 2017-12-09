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
class ActivateUserAcceptanceSpec extends Specification {

    @Autowired
    AccessControlFacade facade

    def "should activate user"() {
        given: "non-activated user present"
        def activationHash = facade.registerUser(MotherObject.registerUser1())
        assert facade.user(MotherObject.USER_NAME_1).get().isActivated() == false

        when:
        facade.activateUser(activationHash)
        then:
        facade.user(MotherObject.USER_NAME_1).get().isActivated()
    }

    def "should reject activation of already active user"() {
        given: "activated user present"
        def activationHash = facade.registerUser(MotherObject.registerUser1())
        facade.activateUser(activationHash)
        assert facade.user(MotherObject.USER_NAME_1).get().isActivated()

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
