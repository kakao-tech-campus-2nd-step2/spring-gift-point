package gift.application.member.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MemberKakaoModel {

    public record MemberInfo(
        @JsonProperty("properties")
        Properties properties,
        @JsonProperty("kakao_account")
        KakaoAccount kakaoAccount
    ) {

    }

    public record Properties(
        @JsonProperty("nickname")
        String nickname
    ) {

    }

    public record KakaoAccount(
        @JsonProperty("profile") Profile profile,
        @JsonProperty("email")
        String email
    ) {

    }

    public record Profile(
        @JsonProperty("nickname")
        String nickname
    ) {

    }

}
