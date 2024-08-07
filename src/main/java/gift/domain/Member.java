package gift.domain;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "member", uniqueConstraints = @UniqueConstraint(columnNames = {"email", "loginType"}))
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LoginType loginType;

    @Column(nullable = false)
    private int point;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<WishlistItem> wishlistItems = new ArrayList<>();

    @OneToOne(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private TokenAuth tokenAuth;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Order> orders = new ArrayList<>();

    public Member() {
    }

    public Member(String email, String password, LoginType loginType) {
        this.email = email;
        this.password = password;
        this.loginType = loginType;
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

    public String getPassword() {
        return password;
    }

    public int getPoint() {
        return point;
    }

    public void setTokenAuth(TokenAuth tokenAuth) {
        this.tokenAuth = tokenAuth;
    }

    public TokenAuth getTokenAuth() {
        return tokenAuth;
    }

    public LoginType getLoginType() {
        return loginType;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public List<Order> getOrders() {
        return orders;
    }
}
