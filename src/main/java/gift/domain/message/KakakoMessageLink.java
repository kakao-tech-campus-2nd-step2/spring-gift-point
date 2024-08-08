package gift.domain.message;

import com.fasterxml.jackson.annotation.JsonProperty;

public record KakakoMessageLink(
    @JsonProperty("web_url")
    String webUrl,
    @JsonProperty("mobile_web_url")
    String mobileWebUrl
) {

}
