package gift.dto.user;

import gift.common.enums.Role;

public class LoginResponse {
    public record Info(
            String name,
            Role role
    ){

    }
}
