package gift.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import gift.member.domain.*;

public record KakaoMemberResponseDto(
        @JsonProperty("id") Long id,
        @JsonProperty("kakao_account") KakaoAccount kakaoAccount
) {
    public Member toMember() {
        Email email = new Email(kakaoAccount.profile().nickname() + "@kakao");
        Nickname nickname = new Nickname(kakaoAccount.profile().nickname());

        return new Member(null, MemberType.USER, email, new Password(""), nickname);
    }

    public record KakaoAccount(
            @JsonProperty("profile") Profile profile
    ) {
    }

    public record Profile(
            @JsonProperty("nickname") String nickname,
            @JsonProperty("profile_image_url") String profileImageUrl
    ) {
    }
}

