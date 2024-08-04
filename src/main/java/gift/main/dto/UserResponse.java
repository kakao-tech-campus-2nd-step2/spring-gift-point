package gift.main.dto;

import gift.main.entity.User;

public record UserResponse(long id,
                           String email,
                           int point) {

    public UserResponse(User user) {
        this(user.getId(), user.getEmail(),user.getPoint());
    }
}
