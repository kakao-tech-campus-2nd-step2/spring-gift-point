package gift.api.order.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record TemplateObject(
    String objectType,
    String text,
    Link link,
    String buttonTitle
) {

    public static TemplateObject of(String text, String webUrl, String mobileWebUrl, String buttonTitle) {
        return new TemplateObject("text", text, Link.of(webUrl, mobileWebUrl), buttonTitle);
    }
}
