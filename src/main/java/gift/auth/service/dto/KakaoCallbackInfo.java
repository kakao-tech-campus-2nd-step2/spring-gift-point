package gift.auth.service.dto;

public record KakaoCallbackInfo(
        String email
) {
    public static KakaoCallbackInfo of(String email) {
        return new KakaoCallbackInfo(email);
    }
}
