package gift.domain.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class KakaoUserInfo {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("kakao_account")
    private KakaoAccount kakaoAccount;

    public static class KakaoAccount {

        private String email;

        public String getEmail() {
            return email;
        }
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        if (kakaoAccount != null) {
            return kakaoAccount.getEmail();
        }
        return null;
    }
}