package gift.DTO;

public record KakaoToken(
        String token_type,
        String access_token,
        Integer expires_in,
        String refresh_token,
        Integer refresh_token_expires_in
) {
}
