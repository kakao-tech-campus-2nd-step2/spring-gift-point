package gift.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "포인트 응답 DTO")
public class MemberPointResponseDTO {

    @Schema(description = "포인트")
    private long point;

    public MemberPointResponseDTO() {
    }

    public MemberPointResponseDTO(long point) {
        this.point = point;
    }

    public long getPoint() {
        return point;
    }
}
