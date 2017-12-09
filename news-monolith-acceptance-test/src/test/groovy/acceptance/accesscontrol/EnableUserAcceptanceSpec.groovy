package acceptance.accesscontrol

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.DirtiesContext
import pl.pja.s13868.news.mono.accesscontrol.domain.AccessControlFacade
import pl.pja.s13868.news.mono.accesscontrol.domain.AccessControlJavaConfig
import pl.pja.s13868.news.mono.accesscontrol.domain.dto.EnableDisableUserDto
import spock.lang.Specification
import spock.lang.Stepwise

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@SpringBootTest(classes = [AccessControlJavaConfig], webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Stepwise
class EnableUserAcceptanceSpec extends Specification {

    @Autowired
    AccessControlFacade facade

    def "should enable user"() {
        given: "the disabled user present"
        facade.registerUser(MotherObject.registration())
        facade.enableOrDisableUser(new EnableDisableUserDto(MotherObject.USER_NAME, false))
        assert facade.user(MotherObject.USER_NAME).get().isEnabled() == false

        when:
        facade.enableOrDisableUser(new EnableDisableUserDto(MotherObject.USER_NAME, true))

        then:
        facade.user(MotherObject.USER_NAME).get().isEnabled()
    }

    def "should reject enablement of non-existing user"() {

        when: "enable user that does not exist"
        facade.enableOrDisableUser(new EnableDisableUserDto('non-existing-user', true))

        then: "system rejects the enablement"
        thrown(IllegalStateException)
    }

}
