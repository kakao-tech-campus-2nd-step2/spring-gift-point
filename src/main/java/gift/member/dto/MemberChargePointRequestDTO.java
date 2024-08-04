package gift.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "포인트 충전 요청 DTO")
public class MemberChargePointRequestDTO {

    @Schema(description = "충전하려는 사용자의 이메일")
    private String email;

    @Schema(description = "충전하고자 하는 포인트")
    private long point;

    public MemberChargePointRequestDTO() {

    }

    public MemberChargePointRequestDTO(String email, long point) {
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
