package gift.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;

import java.util.ArrayList;
import java.util.List;

@Schema(description = "멤버")
@Entity
@Table(name = "member")
public class Member {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Schema(description = "멤버 고유 id ")
    private Long id;

    @Column(unique = true)
    @Email
    @Schema(description = "멤버 email ")
    private String email;
    @Column()
    @Schema(description = "멤버 비밀번호")
    private String password;
    @Column()
    @Schema(description = "멤버 토큰")
    private String token;
    @Column()
    @Schema(description = "멤버 point")
    private Long point;
    @Column()
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    @Schema(description = "멤버가 갖고있는 위시 리스트")
    private final List<Wish> wishes = new ArrayList<>();

    public Member() {
    }

    public Member(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public Member(Long id, String email, String token, Long point) {
        this.id = id;
        this.email = email;
        this.token = token;
        this.point = point;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public List<Wish> getWishes() {
        return wishes;
    }

    public String getToken() {
        return token;
    }

    public Long getPoint() {
        return point;
    }

}
