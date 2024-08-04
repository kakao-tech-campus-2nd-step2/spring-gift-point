package gift.kakao.dto;

public record KakaoProfileDto(
    String id,
    KakaoAccount kakao_account
) {

    public record KakaoAccount(
        String email
    ) {

    }
}
