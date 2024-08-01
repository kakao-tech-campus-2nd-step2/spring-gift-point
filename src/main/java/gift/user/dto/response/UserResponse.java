package gift.user.dto.response;


public record UserResponse(
    String token
) {

    public static UserResponse from(String token) {
        return new UserResponse(token);
    }
}
