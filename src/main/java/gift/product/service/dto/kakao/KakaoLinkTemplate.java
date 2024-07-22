package gift.product.service.dto.kakao;

import com.fasterxml.jackson.annotation.JsonProperty;

public record KakaoLinkTemplate(
        @JsonProperty("web_url")
        String webURL,

        @JsonProperty("mobile_web_url")
        String mobileWebURL
) {
    public static KakaoLinkTemplate of(String url) {
        return new KakaoLinkTemplate(null, null);
    }
}
