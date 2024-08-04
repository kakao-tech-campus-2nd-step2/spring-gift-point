package gift.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "user_table")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false)
    private String password;
    private String role;
    private String accessToken;
    private int point;

    @OneToMany(mappedBy = "user")
    private List<Wish> wishList = new ArrayList<>();

    protected User() {
    }

    public User(String email, String password, String role) {
        this.email = email;
        this.password = password;
        this.role = role;
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

    public String getRole() {
        return role;
    }

    public void updateAccessToken(String accessToken){
        this.accessToken = accessToken;
    }
    public String getAccessToken() {
        return accessToken;
    }
    public void usePoint(int point){
        if (this.point < point){
            throw new IllegalArgumentException("남은 포인트가 사용하려는 포인트보다 적습니다.");
        }
        this.point -= point;
    }
    public void updateRole(Role role) {
        this.role = role.name();
    }
}
