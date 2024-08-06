package gift.doamin.user.entity;

import gift.doamin.product.entity.Product;
import gift.doamin.wishlist.entity.Wish;
import gift.global.AuditingEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;

@Entity
@Table(name = "users")
public class User extends AuditingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    private String name;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    @Column(nullable = false)
    private Integer point;

    @OneToMany(mappedBy = "user")
    private List<Product> products;

    @OneToMany(mappedBy = "user")
    private List<Wish> wishList;

    public User(String email, String password, String name, UserRole role) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.role = role;
        this.point = 0;
    }

    protected User() {

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

    public String getName() {
        return name;
    }

    public UserRole getRole() {
        return role;
    }

    public List<Product> getProducts() {
        return products;
    }

    public List<Wish> getWishList() {
        return wishList;
    }

    public Integer getPoint() {
        return point;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void addPoint(Integer point) {
        if (this.point > Integer.MAX_VALUE - point) {
            throw new IllegalArgumentException("포인트는 " + Integer.MAX_VALUE + "를 초과할 수 없습니다.");
        }
        this.point += point;
    }

    public void subtractPoint(Integer price) {
        if (this.point < price) {
            throw new IllegalArgumentException("보유중인 포인트가 부족합니다.");
        }
        this.point -= price;
    }
}
