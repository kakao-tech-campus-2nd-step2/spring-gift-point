package gift.entity;

import jakarta.persistence.*;

@Entity
public class Member {

    private static final int INITIAL_POINT_BALANCE = 0;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "point_id")
    private Point point;

    public Member(String email, String password) {
        this.email = email;
        this.password = password;
        this.point = new Point(INITIAL_POINT_BALANCE);
    }

    protected Member() {
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public int getPointBalance() {
        return point.getBalance();
    }

    public void subtractPoint(int discountedPrice) {
        point.subtractBalance(discountedPrice);
    }

    public void updatePoint(int newPoint) {
        this.point.updateBalance(newPoint);
    }
}
