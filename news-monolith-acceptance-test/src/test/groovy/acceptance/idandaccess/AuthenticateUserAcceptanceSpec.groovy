package acceptance.idandaccess

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import pl.pja.s13868.news.mono.identityandaccess.domain.IdAndAccessFacade
import pl.pja.s13868.news.mono.identityandaccess.domain.IdAndAccessJavaConfig
import pl.pja.s13868.news.mono.identityandaccess.domain.dto.AuthenticateUserDto
import pl.pja.s13868.news.mono.identityandaccess.domain.dto.UserDetailsDto
import spock.lang.Specification
import spock.lang.Stepwise

@SpringBootTest(classes = [IdAndAccessJavaConfig], webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Stepwise
class AuthenticateUserAcceptanceSpec extends Specification {

    @Autowired
    IdAndAccessFacade facade

    def "should authenticate predefined user"() {
        given: "the active user present in the system"
        assert IdAndAccessFacade.PREDEFINED_USER_NAME == 'user'
        assert IdAndAccessFacade.PREDEFINED_USER_PASS == 'secret'
        assert IdAndAccessFacade.PREDEFINED_USER_EMAIL == 'krzysztof.dzido@gmail.com'

        when:
        def credentials = new AuthenticateUserDto(
                IdAndAccessFacade.PREDEFINED_USER_NAME,
                IdAndAccessFacade.PREDEFINED_USER_PASS)
        Optional<UserDetailsDto> details = facade.authenticate(credentials)

        then:
        details.present
        details.get().userName == IdAndAccessFacade.PREDEFINED_USER_NAME
        details.get().email == IdAndAccessFacade.PREDEFINED_USER_EMAIL
    }

    def "should reject authentication of non-existing user"() {
        when:
        def credentials = new AuthenticateUserDto(
                'non_existing_user_name',
                'secret')
        Optional<UserDetailsDto> details = facade.authenticate(credentials)

        then:
        details.present == false
    }

}
