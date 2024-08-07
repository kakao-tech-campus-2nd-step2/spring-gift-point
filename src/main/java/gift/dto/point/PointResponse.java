package gift.dto.point;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PointResponse {
    private Long memberId;
    private int balance;
    private String message;

    public PointResponse() {}

    public PointResponse(Long memberId, int balance) {
        this.memberId = memberId;
        this.balance = balance;
    }

    public PointResponse(String message) {
        this.message = message;
    }
}
