package gift.controller.dto.response;

import gift.common.enums.Role;
import gift.service.dto.LoginDto;

public record LoginResponse(
        String name,
        Role role) {
    public static LoginResponse from(LoginDto loginDto) {
        return new LoginResponse(loginDto.name(), loginDto.role());
    }
}
