package gift.dto.kakao.template;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record KakaoTemplateContent(
        @JsonProperty("title")
        String title,
        @JsonProperty("image_url")
        String imageUrl,
        @JsonProperty("description")
        String description,
        @JsonProperty("link")
        KakaoTemplateLink link
) {
}

