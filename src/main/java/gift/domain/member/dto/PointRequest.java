package gift.domain.member.dto;

import jakarta.validation.constraints.Min;

public class PointRequest {

    @Min(value = 1, message = "1원 이상 입력해주세요")
    private int point;

    public PointRequest() {
    }

    public PointRequest(int point) {
        this.point = point;
    }

    public int getPoint() {
        return point;
    }
}
