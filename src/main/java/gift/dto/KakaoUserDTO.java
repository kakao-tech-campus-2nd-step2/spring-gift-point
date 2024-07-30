package gift.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class KakaoUserDTO {
    @JsonProperty("id")
    private Long id;

    @JsonProperty("kakao_account")
    private KakaoAccount kakaoAccount;

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return kakaoAccount.email;
    }

    public static class KakaoAccount {
        @JsonProperty("email")
        private String email;

    }
}
