package gift.product.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class PointDTO {

    @NotNull(message = "포인트를 추가할 사용자 정보가 입력되지 않았습니다.")
    private Long memberId;

    @Positive(message = "추가하는 포인트는 1이상의 양의 정수여야 합니다.")
    private int point;

    public PointDTO() {}

    public PointDTO(PointDTO pointDTO) {
        this.memberId = pointDTO.getMemberId();
        this.point = pointDTO.getPoint();
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId =memberId;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }
}
