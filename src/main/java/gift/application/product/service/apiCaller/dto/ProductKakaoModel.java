package gift.application.product.service.apiCaller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ProductKakaoModel {

    public record TemplateObject(
        @JsonProperty("object_type")
        String objectType,
        String text,
        Link link
    ) {

    }

    public record Link(
        @JsonProperty("object_type")
        String webUrl,
        @JsonProperty("mobile_web_url")
        String mobileWebUrl
    ) {

    }

}
