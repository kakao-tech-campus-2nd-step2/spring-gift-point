package gift.model;


import gift.exceptions.CustomException;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.CascadeType;

import java.util.List;


@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name="password", nullable = false)
    private String password;

    @Column(name="point", nullable = false)
    private Long point;

    @Column(name="role", nullable = false)
    private String role;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<Wish> wishes;

    public User() { }

    public User(String email, String password, String role, Long point) {
        this.email = email;
        this.password = password;
        this.role = role;
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

    public Long getPoint() { return point; }

    public String getRole() {
        return role;
    }

    public Long subtractPoint(Long price, Long quantity) {
        Long productPrice = price * quantity;

        if (productPrice > this.point) {
            throw CustomException.pointLackException();
        }

        this.point -= productPrice;
        return this.point;
    }

    public void chargePoint(Long points) {
        this.point += points;
    }
}
