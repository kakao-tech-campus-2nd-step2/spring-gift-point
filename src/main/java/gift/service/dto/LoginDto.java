package gift.service.dto;

import gift.common.enums.Role;

public record LoginDto(
        String accessToken,
        String name,
        Role role
) {
    public static LoginDto of(String accessToken, String name, Role role) {
        return new LoginDto(accessToken, name, role);
    }
}
