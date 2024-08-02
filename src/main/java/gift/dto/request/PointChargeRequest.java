package gift.dto.request;

import jakarta.validation.constraints.Min;

public class PointChargeRequest {

    @Min(value = 1)
    private Integer point;

    public Integer getPoint() {
        return point;
    }

}
