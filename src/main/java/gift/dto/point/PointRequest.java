package gift.dto.point;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PointRequest {
    private Long memberId;
    private Long productId;
    private Long optionId;
    private int quantity;
    private String email;
    private int points;

    public PointRequest() {}

    public PointRequest(Long memberId, int points) {
        this.memberId = memberId;
        this.points = points;
    }

    public PointRequest(Long productId, Long optionId, int quantity, String email) {
        this.productId = productId;
        this.optionId = optionId;
        this.quantity = quantity;
        this.email = email;
    }
}
