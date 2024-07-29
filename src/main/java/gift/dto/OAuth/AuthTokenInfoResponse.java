package gift.dto.OAuth;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public record AuthTokenInfoResponse(
        Long id,
        Integer expiresIn,
        Integer appId
) {

}
