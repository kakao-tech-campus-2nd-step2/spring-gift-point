package gift.auth;

import com.fasterxml.jackson.annotation.JsonProperty;

public record KakaoAccount(
    @JsonProperty("email")
    String email
) {
}
