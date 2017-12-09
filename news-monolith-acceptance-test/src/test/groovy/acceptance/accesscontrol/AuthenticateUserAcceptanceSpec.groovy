package acceptance.accesscontrol

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.DirtiesContext
import pl.pja.s13868.news.mono.accesscontrol.domain.AccessControlFacade
import pl.pja.s13868.news.mono.accesscontrol.domain.AccessControlJavaConfig
import pl.pja.s13868.news.mono.accesscontrol.domain.dto.AuthenticateUserDto
import pl.pja.s13868.news.mono.accesscontrol.domain.dto.EnableDisableUserDto
import pl.pja.s13868.news.mono.accesscontrol.domain.dto.UserDetailsDto
import spock.lang.Specification
import spock.lang.Stepwise

import static acceptance.accesscontrol.MotherObject.*

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@SpringBootTest(classes = [AccessControlJavaConfig], webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Stepwise
class AuthenticateUserAcceptanceSpec extends Specification {

    @Autowired
    AccessControlFacade facade

    def "should authenticate user"() {
        given: "enabled and activated user present in system"
        def activationHash = facade.registerUser(registerUser1())
        facade.activateUser(activationHash)
        assert facade.user(registerUser1().getUserName()).get().isEnabled()
        assert facade.user(registerUser1().getUserName()).get().isActivated()

        when:
        def credentials = new AuthenticateUserDto(USER_NAME_1, USER_PASS_1)
        Optional<UserDetailsDto> details = facade.authenticate(credentials)

        then:
        details.present
        details.get().userName == USER_NAME_1
        details.get().email == USER_EMAIL_1
    }

    def "should reject non-existing user"() {
        given:
        assert facade.user('non_existing_user_name').isPresent() == false

        when:
        def credentials = new AuthenticateUserDto('non_existing_user_name','secret')
        Optional<UserDetailsDto> details = facade.authenticate(credentials)

        then:
        ! details.present
    }

    def "should reject disabled user"() {
        given:
        def activationHash = facade.registerUser(registerDisabledUser())
        facade.activateUser(activationHash)
        facade.enableOrDisableUser(new EnableDisableUserDto(registerDisabledUser().getUserName(), false))
        assert facade.user(registerDisabledUser().getUserName()).get().isActivated()
        assert facade.user(registerDisabledUser().getUserName()).get().isEnabled() == false

        when:
        def credentials = new AuthenticateUserDto(registerDisabledUser().getUserName(), registerDisabledUser().getPassword())
        Optional<UserDetailsDto> details = facade.authenticate(credentials)
        then:
        ! details.present
    }

    def "should reject non-activated user"() {
        given:
        facade.registerUser(registerInactiveUser())
        assert facade.user(registerInactiveUser().getUserName()).get().isEnabled()
        assert facade.user(registerInactiveUser().getUserName()).get().isActivated() == false

        when:
        def credentials = new AuthenticateUserDto(registerInactiveUser().getUserName(), registerInactiveUser().getPassword())
        Optional<UserDetailsDto> details = facade.authenticate(credentials)

        then:
        ! details.present
    }

}
