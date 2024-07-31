package gift.domain.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;

public record KakaoUserInfoResponse(
    @JsonProperty(value = "id") Long id,
    @JsonProperty(value = "connected_at") LocalDateTime connectedAt
) {

}
