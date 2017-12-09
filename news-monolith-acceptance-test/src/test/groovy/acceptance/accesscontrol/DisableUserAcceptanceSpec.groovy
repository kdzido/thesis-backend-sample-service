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
class DisableUserAcceptanceSpec extends Specification {

    @Autowired
    AccessControlFacade facade

    def "should disable user"() {
        given: "the disabled user present"
        facade.registerUser(MotherObject.registration())
        assert facade.user(MotherObject.USER_NAME).get().isEnabled() == true

        when:
        facade.enableOrDisableUser(new EnableDisableUserDto(MotherObject.USER_NAME, false))

        then:
        facade.user(MotherObject.USER_NAME).get().isEnabled() == false
    }

    def "should reject disabling of non-existing user"() {
        when: "enable user that does not exist"
        facade.enableOrDisableUser(new EnableDisableUserDto('non-existig-user', false))

        then: "system rejects the enablement"
        thrown(IllegalStateException)
    }

}
