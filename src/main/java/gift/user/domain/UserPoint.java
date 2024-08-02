package gift.user.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_points")
public class UserPoint {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    private Long point;

    private LocalDateTime createdAt = LocalDateTime.now();

    public UserPoint() {
    }

    public UserPoint(Long userId) {
        this.userId = userId;
        this.point = 0L;
    }

    public void addPoint(Long point) {
        this.point += point;
    }

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getPoint() {
        return point;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void updatePoint(Long newPoint) {
        this.point = newPoint;
    }
}
