package gift.domain.point;

import gift.domain.user.User;
import jakarta.persistence.*;

@Entity
@Table(name = "points")
public class Point {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private Long amount;

    protected Point() {}

    public Point(User user, Long amount) {
        this.user = user;
        this.amount = amount;
    }

    public Long getAmount() {
        return amount;
    }

    public void charge(Long amount) {
        this.amount += amount;
    }

    public void use(Long amount) {
        if (this.amount < amount) {
            throw new IllegalArgumentException("포인트가 부족합니다.");
        }
        this.amount -= amount;
    }

    public void add(Long amount) {
        this.amount += amount;
    }
}
