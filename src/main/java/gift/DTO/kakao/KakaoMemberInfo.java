package gift.DTO.kakao;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;

public record KakaoMemberInfo(
    Long id,
    @JsonProperty("connected_at")
    LocalDateTime connectedAt
){
}
