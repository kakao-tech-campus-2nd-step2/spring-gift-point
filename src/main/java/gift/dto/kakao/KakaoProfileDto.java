package gift.dto.kakao;

public record KakaoProfileDto(
    String id,
    KakaoAccount kakao_account
) {

    public record KakaoAccount(
        String email
    ) {

    }
}
