package gift.domain.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import gift.domain.model.entity.User;

public class UserResponseDto {

    @JsonIgnore
    private String token;

    public UserResponseDto() {
    }

    public UserResponseDto(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}
