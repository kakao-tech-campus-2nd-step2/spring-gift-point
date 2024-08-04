package gift.dto;

import jakarta.validation.constraints.Min;

public class PointRequestDTO {

    @Min(value = 1, message = "추가하려는 Point는 0보다 큰 수여야 합니다.")
    private int point;

    public PointRequestDTO() {
    }

    public PointRequestDTO(int point) {
        this.point = point;
    }

    public int getPoint() {
        return point;
    }
}
