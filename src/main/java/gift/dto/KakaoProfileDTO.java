package gift.dto;

public record KakaoProfileDTO(
    String id,
    KakaoAccount kakao_account
) {

    public record KakaoAccount(
        String email
    ) {

    }
}
