package acceptance.accesscontrol

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.DirtiesContext
import pl.pja.s13868.news.mono.accesscontrol.domain.AccessControlFacade
import pl.pja.s13868.news.mono.accesscontrol.domain.AccessControlJavaConfig
import pl.pja.s13868.news.mono.accesscontrol.domain.dto.EnableDisableUserDto
import spock.lang.Specification
import spock.lang.Stepwise

import static acceptance.accesscontrol.MotherObject.*

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@SpringBootTest(classes = [AccessControlJavaConfig], webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Stepwise
class DisableUserAcceptanceSpec extends Specification {

    @Autowired
    AccessControlFacade facade

    def "should disable user"() {
        given: "the disabled user present"
        facade.registerUser(registerUser1())
        assert facade.user(USER_NAME_1).get().isEnabled() == true

        when:
        facade.enableOrDisableUser(new EnableDisableUserDto(USER_NAME_1, false))

        then:
        facade.user(USER_NAME_1).get().isEnabled() == false
    }

    def "should reject disabling of non-existing user"() {
        when: "enable user that does not exist"
        facade.enableOrDisableUser(new EnableDisableUserDto('non-existig-user', false))

        then: "system rejects the enablement"
        thrown(IllegalStateException)
    }

}
