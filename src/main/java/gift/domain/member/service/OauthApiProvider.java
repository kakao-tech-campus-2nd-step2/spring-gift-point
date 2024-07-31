package gift.domain.member.service;

public interface OauthApiProvider <T, E> {

    String getAuthCodeUrl();
    T getToken(String code);
    void validateAccessToken(String accessToken);
    E getUserInfo(String accessToken);
    T renewToken(String refreshToken);
}
