package gift.dto.kakao.template;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record KakaoTemplate(
        @JsonProperty("object_type")
        String objectType,
        @JsonProperty("content")
        KakaoTemplateContent content,
        @JsonProperty("commerce")
        KakaoTemplateCommerce commerce
) {
}

