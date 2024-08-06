package gift.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class PointUpdateDto {
    @NotNull
    Long id;
    @Min(value = 0, message = "수정 가능한 포인트는 0부터 가능합니다.")
    @Max(value = 30_000_000, message = "충전 가능한 포인트 한도는 최대 3000만원입니다.")
    int point;

    public PointUpdateDto() {
    }

    public PointUpdateDto(Long id, int point) {
        this.id = id;
        this.point = point;
    }

    public Long getId() {
        return id;
    }

    public int getPoint() {
        return point;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setPoint(int point) {
        this.point = point;
    }
}
