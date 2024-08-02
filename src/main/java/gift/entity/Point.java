package gift.entity;

import gift.exception.BusinessException;
import gift.exception.ErrorCode;
import jakarta.persistence.*;

@Entity
public class Point {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int points;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    protected Point() {
    }

    public Point(User user) {
        this.user = user;
        this.points = 0;
    }

    public Long getId() {
        return id;
    }

    public int getPoints() {
        return points;
    }

    public void addPoints(int points) {
        this.points += points;
    }

    public void subtractPoints(int points) {
        if (this.points < points) {
            throw new BusinessException(ErrorCode.INSUFFICIENT_POINTS);
        }
        this.points -= points;
    }

    public User getUser() {
        return user;
    }
}
