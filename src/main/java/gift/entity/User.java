package gift.entity;

import gift.exception.MinimumOptionException;
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

@Entity
@Table(name = "users")
public class User {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;
    @NotNull
    @Column(unique = true)
    private String email;
    @NotNull
    private String password;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Wish> wishes = new ArrayList<>();
    @NotNull
    private Integer point;
    @NotNull
    private String accessToken;

    protected User() {
        this.point = 0;
        this.accessToken = "demoToken";
    }

    public User(String email, String password) {
        this.email = email;
        this.password = password;
        this.point = 0;
        this.accessToken = "demoToken";
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

    public boolean samePassword(String password) {
        return this.password.equals(password);
    }

//    public void subtractWishNumber(Integer number, Product product) {
//        wishes.removeIf(wish -> {
//            if (wish.sameProduct(product)) {
//                wish.subtractNumber(number);
//            }
//            return wish.checkLeftWishNumber();
//        });
//    }

    public void deleteWish(Product product) {
        this.wishes.removeIf(wish -> wish.sameProduct(product));
    }

    public Integer getPoint() {
        return point;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public boolean isKakaoLoginCompleted() {
        return !this.accessToken.equals("demoToken");
    }

    public void subtractPoint(Integer pointAmount) {
        if ((this.point - pointAmount) < 0) {
            throw new MinimumOptionException("잔여 포인트가 0보다 작을수 없습니다.");
        }
        this.point -= pointAmount;
    }

    public void addPoint(Integer pointAmount) {
        this.point += pointAmount;
    }

    public void updatePoint(Integer point) {
        this.point = point;
    }
}
