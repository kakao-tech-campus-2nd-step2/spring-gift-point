package gift.api.member.dto;

public record KakaoLoginResponse(
    String MemberAccessToken,
    String kakaoAccessToken
) {

    public static KakaoLoginResponse of(String memberAccessToken, String kakaoAccessToken) {
        return new KakaoLoginResponse(memberAccessToken, kakaoAccessToken);
    }
}
