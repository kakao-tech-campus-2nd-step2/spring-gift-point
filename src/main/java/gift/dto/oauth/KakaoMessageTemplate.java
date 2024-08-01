package gift.dto.oauth;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.Length;

public class KakaoMessageTemplate {
    @JsonProperty("object_type")
    private String objectType = "text";

    @JsonProperty("text")
    @Length(max = 200)
    private String text;

    @JsonProperty("link")
    private Link link = new Link("https://developers.kakao.com", "https://developers.kakao.com");

    public KakaoMessageTemplate(String text) {
        this.text = text;
    }

    private record Link(
            @JsonProperty("web_url")
            String webUrl,

            @JsonProperty("mobile_web_url")
            String mobileWebUrl
    ) {
    }
}