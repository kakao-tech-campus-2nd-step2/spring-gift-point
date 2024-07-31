package gift.controller.user.dto;

import gift.model.User;

public record UserResponse(String name) {

    public static UserResponse from(String name) {
        return new UserResponse(name);
    }
}
