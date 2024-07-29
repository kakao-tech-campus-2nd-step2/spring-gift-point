package gift.domain.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import gift.domain.model.entity.User;

public class UserResponseDto {

    private User user;

    @JsonIgnore
    private String token;

    public UserResponseDto(User user, String token) {
        this.user = user;
        this.token = token;
    }

    public User getUser() {
        return user;
    }

    public String getToken() {
        return token;
    }
}
