package gift.service.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

public record KakaoInfoDto(String email, String name) {

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public record Info(
            String id,
            String connectedAt,
            Properties properties
    ) {
    }

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public record Properties(
            String nickname
    ) {
    }
}
