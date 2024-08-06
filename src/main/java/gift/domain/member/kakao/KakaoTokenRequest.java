package gift.domain.member.kakao;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

public record KakaoTokenRequest(
    @JsonProperty("grant_type")
    String grantType,
    @JsonProperty("client_id")
    String clientId,
    @JsonProperty("redirect_url")
    String redirectUrl,
    @JsonProperty("code")
    String code
) {

    public MultiValueMap<String, String> toMap() {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("grant_type", grantType);
        map.add("client_id", clientId);
        map.add("redirect_uri", redirectUrl);
        map.add("code", code);
        return map;
    }
}
