package gift.service;

import gift.common.enums.Role;

public record UserDto(
    String accessToken,
    String name,
    Role role
) {

    public static UserDto from(String accessToken, String name, Role role) {
        return new UserDto(accessToken, name, role);
    }
}
