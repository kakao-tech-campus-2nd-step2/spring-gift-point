package gift.oauth.business.dto;

public class OAuthToken {
    public record Kakao (
        String token_type,
        String access_token,
        Long expires_in,
        String refresh_token,
        Long refresh_token_expires_in,
        String scope
    ) {
        public Common toCommon() {
            return new Common(access_token, refresh_token);
        }
    }

    public record Common (
        String accessToken,
        String refreshToken
    ) {}

}
