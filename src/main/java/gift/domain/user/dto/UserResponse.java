package gift.domain.user.dto;

import gift.domain.user.entity.User;

public record UserResponse(
    Long id,
    String name,
    String email,
    String password,
    String role,
    String authProvider)
{
    public static UserResponse from(User user) {
        return new UserResponse(
            user.getId(),
            user.getName(),
            user.getEmail(),
            user.getPassword(),
            user.getRole().toString(),
            user.getAuthProvider().toString()
        );
    }
}
