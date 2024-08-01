package gift.user.dto.response;


public record UserResponse(
    String tokenType,
    String token
) {

    public static UserResponse from(String token) {
        return new UserResponse("Bearer", token);
    }
}
