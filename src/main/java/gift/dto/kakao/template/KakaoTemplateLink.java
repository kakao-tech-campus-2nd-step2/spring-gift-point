package gift.dto.kakao.template;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record KakaoTemplateLink(
        @JsonProperty("web_url")
        String webUrl
) {
}

