package gift.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "사용자 응답용 DTO")
public class MemberResponseDTO {

    @Schema(description = "사용자 이메일")
    private String email;

    @Schema(description = "사용자 포인트")
    private long point;

    public MemberResponseDTO(String email, long point) {
        this.email = email;
        this.point = point;
    }

    public String getEmail() {
        return email;
    }

    public long getPoint() {
        return point;
    }
}
