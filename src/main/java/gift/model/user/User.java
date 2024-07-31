package gift.model.user;

import gift.model.order.Order;
import gift.model.wishList.WishItem;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.annotations.BatchSize;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    @Column
    private String email;
    @NotNull
    @Column
    private String password;
    @BatchSize(size = 100)
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<WishItem> wishItemList = new ArrayList<>();
    @BatchSize(size = 100)
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<Order> orders = new ArrayList<>();
    @Column
    private Long kakaoId;
    @Column
    private String accessToken;
    @Column
    private String refreshToken;
    @Column
    private Role role;

    protected User() {
    }

    public User(String email, String password, Long kakaoId, String accessToken,
        String refreshToken) {
        this.email = email;
        this.password = password;
        this.kakaoId = kakaoId;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.role = Role.ROLE_USER;
    }

    public User(String email, String password) {
        this(email, password, null, null, null);
    }

    public Long getKakaoId() {
        return kakaoId;
    }

    public Long getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public List<WishItem> getWishItemList() {
        return wishItemList;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public Role getRole() {
        return role;
    }

    public Order addOrder(Order order) {
        this.orders.add(order);
        return order;
    }

}