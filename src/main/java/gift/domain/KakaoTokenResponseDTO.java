package gift.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;

public class KakaoTokenResponseDTO {
    @JsonProperty("token_type")
    public String tokenType;
    @JsonProperty("access_token")
    public String accessToken;
    @JsonProperty("id_token")
    public String idToken;
    @JsonProperty("expires_in")
    public Integer expiresIn;
    @JsonProperty("refresh_token")
    public String refreshToken;
    @JsonProperty("refresh_token_expires_in")
    public Integer refreshTokenExpiresIn;
    @JsonProperty("scope")
    public String scope;
    @JsonProperty("kakao_account")
    public KakaoAccountInfo kakaoAccount;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class KakaoAccountInfo {
        @JsonProperty("email")
        private String email;

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }

    public KakaoTokenResponseDTO() {}

    public String getAccessToken() {
        return accessToken;
    }

    public String getEmail(){
        return kakaoAccount.getEmail();
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public void setKakaoAccount(KakaoAccountInfo kakaoAccount) {
        this.kakaoAccount = kakaoAccount;
    }
}