package gift.dto.kakao;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record KakaoAccount(
        @JsonProperty("profile")
        KakaoProfile profile,
        @JsonProperty("email")
        String email
) {
}

