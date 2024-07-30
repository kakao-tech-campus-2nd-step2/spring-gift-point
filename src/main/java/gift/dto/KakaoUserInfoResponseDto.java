package gift.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Schema(description = "카카오 사용자 정보 응답 DTO")
public record KakaoUserInfoResponseDto(

        @Schema(description = "카카오 사용자 ID")
        Long id
) {
}