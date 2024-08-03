package gift.domain.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "포인트 응답 Dto")
public record PointResponse(
    @Schema(description = "포인트")
    int point
) {
}
