package gift.auth;

public record OAuthToken (
        String access_token,
        String token_type,
        String refresh_token,
        int expires_in,
        String scope,
        int refresh_token_expires_in
) {
}
