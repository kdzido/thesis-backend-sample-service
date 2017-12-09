package acceptance.accesscontrol

import pl.pja.s13868.news.mono.accesscontrol.domain.dto.RegisterUserDto

class MotherObject {

    static USER_NAME = "user"
    static USER_EMAIL = "krzysztof.dzido@gmail.com"
    static USER_PASS = "secret"

    static RegisterUserDto registration() {
        new RegisterUserDto(
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
    }

}
