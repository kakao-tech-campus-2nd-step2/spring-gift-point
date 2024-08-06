package gift.dto.point;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PointRequest {
    private Long memberId;
    private int point;
}
