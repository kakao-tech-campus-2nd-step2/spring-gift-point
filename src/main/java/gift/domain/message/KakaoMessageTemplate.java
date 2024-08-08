package gift.domain.message;

import com.fasterxml.jackson.annotation.JsonProperty;

public record KakaoMessageTemplate(
    @JsonProperty("object_type")
    String objectType,
    @JsonProperty("text")
    String text,
    @JsonProperty("link")
    KakakoMessageLink link,
    @JsonProperty("button_title")
    String buttonTitle
) {

}
