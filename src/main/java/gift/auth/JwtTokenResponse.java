package gift.auth;

public record JwtTokenResponse(
        String token
){
    public static JwtTokenResponse from(String token) {
        return new JwtTokenResponse(token);
    }
}
