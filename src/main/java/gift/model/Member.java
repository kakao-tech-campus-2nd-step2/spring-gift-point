package gift.model;

import gift.exception.OutOfStockException;
import jakarta.persistence.*;

@Entity(name = "members")
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false)
    private String password;
    private String accessToken;
    private int point;

    public Member() {
    }

    public Member(String email, String password) {
        this.email = email;
        this.password = password;
        this.point = 0;
    }

    public Member(String email, String password, String accessToken) {
        this.email = email;
        this.password = password;
        this.accessToken = accessToken;
        this.point = 0;
    }

    public Member(Long id, String email, String password, int point) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.point = point;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public boolean matchPassword(String password) {
        return this.password.equals(password);
    }

    public void updateAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public int getPoint() {
        return point;
    }

    public void subtractPoint(int usedPoint) {
        int remainingPoint = this.point - usedPoint;
        if (remainingPoint < 0) {
            throw new IllegalArgumentException("사용된 포인트가 갖고 있는 포인트보다 큰 값일 수 없습니다.");
        }
        this.point = remainingPoint;
    }

    public void chargePoint(int usingPoint) {
        this.point += usingPoint;
    }
}
