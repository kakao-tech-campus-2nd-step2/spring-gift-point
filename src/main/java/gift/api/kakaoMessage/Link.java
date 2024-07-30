package gift.api.kakaoMessage;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Link(@JsonProperty("web_url") String webUrl,
                   @JsonProperty("mobile_web_url") String mobileWebUrl) {

}
