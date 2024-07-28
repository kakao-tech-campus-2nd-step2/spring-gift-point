package gift.dto;
import com.fasterxml.jackson.annotation.JsonProperty;


public record KakaoMessageDto(
    @JsonProperty("object_type")
    String objectType,
    String text,
    Link link
) {
    public KakaoMessageDto(String objectType, String text, String webUrl, String mobileWebUrl) {
        this(objectType, text, new Link(webUrl, mobileWebUrl));
    }

    public record Link(
        @JsonProperty("web_url")
        String webURL,
        @JsonProperty("mobile_web_url")
        String mobileWebURL
    ) {

    }
}
