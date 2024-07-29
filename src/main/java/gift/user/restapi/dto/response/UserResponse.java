package gift.user.restapi.dto.response;

import gift.core.domain.user.User;

public record UserResponse(
        Long id,
        String name
){
    public static UserResponse of(User user) {
        return new UserResponse(user.id(), user.name());
    }
}
