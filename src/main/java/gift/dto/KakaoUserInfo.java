package gift.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record KakaoUserInfo(Long id,
                            @JsonProperty("connected_at")
                            String connectedAt,
                            @JsonProperty("kakao_account")
                            KakaoAccount kakaoAccount) {
    public static class KakaoAccount {
        @JsonProperty("has_email")
        public Boolean hasEmail;
        @JsonProperty("email_needs_agreement")
        public Boolean emailNeedsAgreement;
        @JsonProperty("is_email_valid")
        public Boolean isEmailValid;
        @JsonProperty("is_email_verified")
        public Boolean isEmailVerified;
        public String email;
    }
}
