package gift.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class PointRequestDto {

    @NotNull
    @Min(value = 1)
    private Integer point;

    public PointRequestDto() {
    }

    public PointRequestDto(Integer point) {
        this.point = point;
    }

    public Integer getPoint() {
        return point;
    }
}