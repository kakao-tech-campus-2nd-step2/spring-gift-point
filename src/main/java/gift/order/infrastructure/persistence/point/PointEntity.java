package gift.order.infrastructure.persistence.point;

import gift.core.domain.order.exception.PointNotEnoughException;
import gift.core.exception.ErrorCode;
import gift.core.exception.validation.InvalidArgumentException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "point")
public class PointEntity {
    @Id
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "point")
    private Long point;

    public static PointEntity from(Long userId, Long point) {
        return new PointEntity(userId, point);
    }

    protected PointEntity() {
    }

    protected PointEntity(Long userId, Long point) {
        this.userId = userId;
        this.point = point;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getPoint() {
        return point;
    }

    public void addPoint(Long point) {
        if (point < 0) {
            throw new InvalidArgumentException(ErrorCode.NEGATIVE_POINT);
        }
        this.point += point;
    }

    public void subtractPoint(Long point) {
        if (this.point < point) {
            throw new PointNotEnoughException();
        }
        this.point -= point;
    }
}
