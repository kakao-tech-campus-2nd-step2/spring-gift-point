package gift.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "member")
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false)
    private String password;
    private String accessToken;
    private Integer points = 0;

    protected Member() {
    }

    public Member(Long id, String email, String password) {
        this.id = id;
        this.email = email;
        this.password = password;
    }

    public Member(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public Member(String email, String password, String accessToken) {
        this.email = email;
        this.password = password;
        this.accessToken = accessToken;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public Integer getPoints() {
        return points;
    }

    public void addPoints(int price, int quantity) {
        if (this.points == null) {
            this.points = 0;
        }
        this.points += (int) Math.round(price * quantity * 0.01);
    }

    public void subtractPoints(int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("사용할 포인트 값은 0, 양수만 가능하다.");
        }
        if (points < amount) {
            throw new IllegalStateException("사용할 포인트 값은 기존 포인트보다 작아야 한다.");
        }
        this.points -= amount;
    }
}
