package gift.member.model;

import gift.exception.IllegalEmailException;
import gift.order.model.Order;
import gift.wish.model.Wish;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Entity
@Table(name = "member")
@Getter
@ToString
@EqualsAndHashCode
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "kakao_id", unique = true)
    private String kakaoId;

    @OneToMany(mappedBy = "member")
    private List<Order> orders;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<Wish> wishList = new ArrayList<>();

    public Member() {
    }

    public Member(String kakaoId) {
        this.kakaoId = kakaoId;
    }

    public Member(String email, String password) throws IllegalEmailException {
        this(email, password, null);
    }

    public Member(String email, String password, String kakaoId) throws IllegalEmailException {
        if (!email.contains("@"))
            throw new IllegalEmailException("올바른 이메일 형식이 아닙니다.");
        this.email = email;
        this.password = password;
        this.kakaoId = kakaoId;
    }


    public @Email @NotBlank String getEmail() {
        return email;
    }

    public void setEmail(@Email @NotBlank String email) {
        this.email = email;
    }

    public @NotBlank String getPassword() {
        return password;
    }

    public void setPassword(@NotBlank String password) {
        this.password = password;
    }

    public void setId(Long memberId) {
        this.id = memberId;
    }

    public void setKakaoId(String hashedKakaoId) {
        this.kakaoId = hashedKakaoId;
    }
}
