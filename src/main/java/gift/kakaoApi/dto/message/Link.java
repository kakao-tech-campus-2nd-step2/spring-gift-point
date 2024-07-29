package gift.kakaoApi.dto.message;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record Link(
    String webUrl,
    String mobileWebUrl
) {

}
