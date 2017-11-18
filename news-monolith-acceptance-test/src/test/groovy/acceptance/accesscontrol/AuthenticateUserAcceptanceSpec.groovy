package acceptance.accesscontrol

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.DirtiesContext
import pl.pja.s13868.news.mono.accesscontrol.domain.IdAndAccessFacade
import pl.pja.s13868.news.mono.accesscontrol.domain.IdAndAccessJavaConfig
import pl.pja.s13868.news.mono.accesscontrol.domain.dto.AuthenticateUserDto
import pl.pja.s13868.news.mono.accesscontrol.domain.dto.EnableDisableUserDto
import pl.pja.s13868.news.mono.accesscontrol.domain.dto.RegisterUserDto
import pl.pja.s13868.news.mono.accesscontrol.domain.dto.UserDetailsDto
import spock.lang.Specification
import spock.lang.Stepwise

@DirtiesContext
@SpringBootTest(classes = [IdAndAccessJavaConfig], webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Stepwise
class AuthenticateUserAcceptanceSpec extends Specification {

    final USER_NAME = "user"
    final USER_EMAIL = "krzysztof.dzido@gmail.com"
    final USER_PASS = "secret"

    @Autowired
    IdAndAccessFacade facade

    def registration = new RegisterUserDto(
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


    def "should authenticate user"() {
        given: "the enabled user present in the system"
        facade.registerUser(registration)
        facade.enableUser(new EnableDisableUserDto(USER_NAME, true))

        when:
        def credentials = new AuthenticateUserDto(
                USER_NAME,
                USER_PASS)
        Optional<UserDetailsDto> details = facade.authenticate(credentials)

        then:
        details.present
        details.get().userName == USER_NAME
        details.get().enabled == true
        details.get().email == USER_EMAIL
    }

    def "should reject authentication of non-existing user"() {
        when:
        def credentials = new AuthenticateUserDto('non_existing_user_name','secret')
        Optional<UserDetailsDto> details = facade.authenticate(credentials)

        then:
        details.present == false
    }

}
