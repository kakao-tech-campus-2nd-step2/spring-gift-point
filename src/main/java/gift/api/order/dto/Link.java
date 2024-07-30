package gift.api.order.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record Link(
    String webUrl,
    String mobileWebUrl
) {

    public static Link of(String webUrl, String mobileWebUrl) {
        return new Link(webUrl, mobileWebUrl);
    }
}
