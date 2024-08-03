package gift.domain.model.dto;

public class UserResponseDto {

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
