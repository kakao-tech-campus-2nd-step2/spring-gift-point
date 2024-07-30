package gift.domain.member;

import gift.domain.BaseTimeEntity;
import gift.domain.member.kakao.KaKaoToken;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import java.util.Objects;

@Entity
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true)
    private String email;

    @NotNull
    private String password;

    @Enumerated(EnumType.STRING)
    private MemberRole role;

    private String accessToken;
    private String refreshToken;

    protected Member() {
    }

    public Member(String email, String password) {
        this.email = email;
        this.password = password;
        this.role = MemberRole.USER;
    }

    public Member(String email, String password, String accessToken, String refreshToken) {
        this.email = email;
        this.password = password;
        this.role = MemberRole.USER;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
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

    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    @Override
    public String toString() {
        return "User{" +
               "id=" + id +
               ", email='" + email + '\'' +
               ", password='" + password + '\'' +
               '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Member member = (Member) o;
        return Objects.equals(id, member.id) &&
               Objects.equals(email, member.email) &&
               Objects.equals(password, member.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, password);
    }

    public void updateKaKaoToken(KaKaoToken kaKaoToken) {
        this.accessToken = kaKaoToken.accessToken();
        this.refreshToken = kaKaoToken.refreshToken();
    }
}
