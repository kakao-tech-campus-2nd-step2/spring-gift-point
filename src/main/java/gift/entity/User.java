package gift.entity;

import gift.dto.user.UserRequestDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "유저 id", nullable = false, example = "1")
    private Long id;
    @Schema(description = "유저 이메일", nullable = false, example = "test@mail.com")
    private String email;
    @Schema(description = "유저 비밀번호", nullable = false, example = "pa_sSWo_R_d")
    private String password;
    @Schema(description = "유저 포인트", nullable = false, example = "1")
    private int point;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Product> products;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Option> options;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Order> orders;

    public User() {
    }

    public User(String email, String password) {
        this.email = email;
        this.password = password;
        this.point = 0;
    }

    public User(UserRequestDTO user) {
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.point = 0;
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

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public void addPoint(int amount) {
        this.point += amount;
    }

    public void subtractPoint(int amount) {
        this.point -= amount;
    }
}
