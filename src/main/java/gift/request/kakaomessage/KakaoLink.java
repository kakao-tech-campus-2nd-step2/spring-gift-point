package gift.request.kakaomessage;

import com.fasterxml.jackson.annotation.JsonProperty;

public record KakaoLink(

    @JsonProperty("web_url")
    String webUrl,
    @JsonProperty("mobile_web_url")
    String mobileWebUrl
) {

    private static final String DEFAULT_WEB_URL = "";
    private static final String DEFAULT_MOBILE_WEB_URL = "";

    public static KakaoLink createLink() {
        return new KakaoLink(DEFAULT_WEB_URL, DEFAULT_MOBILE_WEB_URL);
    }

}
