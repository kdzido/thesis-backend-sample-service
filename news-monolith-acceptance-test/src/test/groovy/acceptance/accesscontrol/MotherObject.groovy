package acceptance.accesscontrol

import pl.pja.s13868.news.mono.accesscontrol.domain.dto.RegisterUserDto

class MotherObject {

    static USER_NAME_1 = "user"
    static USER_EMAIL_1 = "krzysztof.dzido@gmail.com"
    static USER_PASS_1 = "secret"

    static RegisterUserDto registerUser1() {
        new RegisterUserDto(
                USER_NAME_1,
                USER_PASS_1,
                "First",
                "Last",
                USER_EMAIL_1,
                null,
                null,
                "Bio...",
                "Poland",
                "Lublin")
    }

    static RegisterUserDto registerDisabledUser() {
        new RegisterUserDto(
                "disabled",
                USER_PASS_1,
                "Disabled",
                "User",
                USER_EMAIL_1,
                null,
                null,
                "Bio...",
                "Poland",
                "Krakow")
    }

    static RegisterUserDto registerInactiveUser() {
        new RegisterUserDto(
                "inacive",
                USER_PASS_1,
                "Inactive",
                "User",
                USER_EMAIL_1,
                null,
                null,
                "Bio...",
                "Poland",
                "Lodz")
    }

}
