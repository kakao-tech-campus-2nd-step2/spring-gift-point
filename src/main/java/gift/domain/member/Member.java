package gift.domain.member;

import gift.exception.CustomException;
import gift.exception.ErrorCode;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique = true)
    @NotNull
    private String email;

    @NotNull
    private String name;

    @NotNull
    private String password;

    private String accessToken;

    private String refreshToken;
    private int role;

    @NotNull
    private int point;

    public Member(String email, String name, String password, int role) {
        this(email, name, password, null, null, role);
    }

    public Member(String email, String name, String password, String accessToken, String refreshToken, int role) {
        this.email = email;
        this.name = name;
        this.password = password;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.role = role;
        this.point = 0;
    }

    protected Member() {
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public int getPoint() {
        return point;
    }

    public boolean isMatch(String password) {
        return password.equals(this.password);
    }

    public void buyProduct(int price) {
        if (checkPoint(price)) {
            point = point - price;
        }
    }

    public boolean checkPoint(int price) {
        if (price > this.point) {
            throw new CustomException(ErrorCode.NOT_ENOUGH_POINT);
        }
        return true;
    }

    public void updatePoint(int point) {
        this.point = point;
    }
}
