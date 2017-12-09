package acceptance.accesscontrol

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.DirtiesContext
import pl.pja.s13868.news.mono.accesscontrol.domain.AccessControlFacade
import pl.pja.s13868.news.mono.accesscontrol.domain.AccessControlJavaConfig
import pl.pja.s13868.news.mono.accesscontrol.domain.dto.AuthenticateUserDto
import pl.pja.s13868.news.mono.accesscontrol.domain.dto.EnableDisableUserDto
import pl.pja.s13868.news.mono.accesscontrol.domain.dto.RegisterUserDto
import pl.pja.s13868.news.mono.accesscontrol.domain.dto.UserDetailsDto
import spock.lang.Specification
import spock.lang.Stepwise

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@SpringBootTest(classes = [AccessControlJavaConfig], webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Stepwise
class AuthenticateUserAcceptanceSpec extends Specification {

    final USER_NAME = "user"
    final USER_EMAIL = "krzysztof.dzido@gmail.com"
    final USER_PASS = "secret"

    @Autowired
    AccessControlFacade facade

    def registerValid = new RegisterUserDto(
            USER_NAME,
            USER_PASS,
            "Full",
            "Name",
            USER_EMAIL,
            null,
            null,
            "Bio...",
            "Poland",
            "Lublin")

    def registerDisabled = new RegisterUserDto(
            "disabled",
            USER_PASS,
            "Disabled",
            "User",
            USER_EMAIL,
            null,
            null,
            "Bio...",
            "Poland",
            "Krakow")

    def registerInactive = new RegisterUserDto(
            "inacive",
            USER_PASS,
            "Inactive",
            "User",
            USER_EMAIL,
            null,
            null,
            "Bio...",
            "Poland",
            "Lodz")

    def "should authenticate user"() {
        given: "enabled and activated user present in system"
        def activationHash = facade.registerUser(registerValid)
        facade.activateUser(activationHash)
        assert facade.user(registerValid.getUserName()).get().isEnabled()
        assert facade.user(registerValid.getUserName()).get().isActivated()

        when:
        def credentials = new AuthenticateUserDto(USER_NAME, USER_PASS)
        Optional<UserDetailsDto> details = facade.authenticate(credentials)

        then:
        details.present
        details.get().userName == USER_NAME
        details.get().email == USER_EMAIL
    }

    def "should reject authentication of non-existing user"() {
        given:
        assert facade.user('non_existing_user_name').isPresent() == false

        when:
        def credentials = new AuthenticateUserDto('non_existing_user_name','secret')
        Optional<UserDetailsDto> details = facade.authenticate(credentials)

        then:
        ! details.present
    }

    def "should reject authentication of disabled user"() {
        given:
        def activationHash = facade.registerUser(registerDisabled)
        facade.activateUser(activationHash)
        facade.enableOrDisableUser(new EnableDisableUserDto(registerDisabled.getUserName(), false))
        assert facade.user(registerDisabled.getUserName()).get().isActivated()
        assert facade.user(registerDisabled.getUserName()).get().isEnabled() == false

        when:
        def credentials = new AuthenticateUserDto(registerDisabled.getUserName(), registerDisabled.getPassword())
        Optional<UserDetailsDto> details = facade.authenticate(credentials)
        then:
        ! details.present
    }

    def "should reject authentication of non-activated user"() {
        given:
        facade.registerUser(registerInactive)
        assert facade.user(registerInactive.getUserName()).get().isEnabled()
        assert facade.user(registerInactive.getUserName()).get().isActivated() == false

        when:
        def credentials = new AuthenticateUserDto(registerInactive.getUserName(), registerInactive.getPassword())
        Optional<UserDetailsDto> details = facade.authenticate(credentials)

        then:
        ! details.present
    }

}
