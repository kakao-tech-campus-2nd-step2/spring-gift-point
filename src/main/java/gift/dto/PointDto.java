package gift.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.PositiveOrZero;

@Schema(description = "포인트 요청/반환 DTO")
public record PointDto (

        @PositiveOrZero
        @Schema(description = "사용자의 포인트")
        int point
) {
}