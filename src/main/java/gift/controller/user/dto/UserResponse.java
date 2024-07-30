package gift.controller.user.dto;

import gift.model.User;

public record UserResponse(String accessToken) {

    public static UserResponse from(String accessToken) {
        return new UserResponse(accessToken);
    }
}
