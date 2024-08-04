package gift.auth;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public record KakaoMessageSend(
    String objectType,
    String text,
    Link link,
    String buttonTitle
) {
    public record Link(
        String webUrl,
        String mobileWebUrl
    ) {
    }

    public KakaoMessageSend (String text) {
        this("text", text, new Link("example.com", "example.com"), "확인하기");
    }
}
