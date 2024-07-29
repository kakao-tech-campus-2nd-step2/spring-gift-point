package gift.controller.user.dto;

import gift.model.User;

public record UserResponse(Long id, String password, String email) {

    public static UserResponse from(User user) {
        return new UserResponse(user.getId(), user.getPassword(), user.getEmail());
    }
}
