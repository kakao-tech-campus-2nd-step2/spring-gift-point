package gift.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class KakaoMessageTemplate {
    @JsonProperty("object_type")
    private String objectType = "text";

    @JsonProperty("text")
    private String text;

    @JsonProperty("link")
    private Link link = new Link();

    @JsonProperty("button_title")
    private String buttonTitle = "자세히 보기";

    public KakaoMessageTemplate(String text) {
        this.text = text;
    }

    private static class Link {
        @JsonProperty("web_url")
        String webUrl = "https://developers.kakao.com";
        @JsonProperty("mobile_web_url")
        String mobileWebUrl = "https://developers.kakao.com";
    }
}
