package gift.web.dto.response.member;

import gift.domain.vo.Point;

public class PointResponse {

    private final int point;

    public PointResponse(int point) {
        this.point = point;
    }

    public static PointResponse from(Point point) {
        return new PointResponse(point.getValue());
    }

    public int getPoint() {
        return point;
    }
}
