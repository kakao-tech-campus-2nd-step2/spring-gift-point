package gift.user.domain;

public class KakaoUserRegisterRequest implements UserRegisterRequest {
    private String name;
    private String profileImageUrl;
    private Long kakaoId;

    public KakaoUserRegisterRequest(String name, String profileImageUrl, Long kakaoId) {
        this.name = name;
        this.profileImageUrl = profileImageUrl;
        this.kakaoId = kakaoId;
    }

    @Override
    public String getName() {
        return name;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public Long getKakaoId() {
        return kakaoId;
    }

    @Override
    public LoginType getLoginType() {
        return LoginType.KAKAO;
    }
}
