package gift.dto.kakao.template;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record KakaoTemplateCommerce(
        @JsonProperty("product_name")
        String productName,
        @JsonProperty("regular_price")
        Integer regularPrice
) {
}

