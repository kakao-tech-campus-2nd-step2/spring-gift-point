package gift.dto.oauth;

public record KakaoTokenResponse(
    String accessToken,
    Integer expiresIn,
    String refreshToken,
    Integer refreshTokenExpiresIn
) {

}
