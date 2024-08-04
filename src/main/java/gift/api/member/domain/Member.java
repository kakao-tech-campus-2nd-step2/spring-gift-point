package gift.api.member.domain;

import gift.api.member.enums.Role;
import gift.api.member.exception.NotEnoughPointException;
import gift.global.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(name = "uk_member", columnNames = {"email"}))
public class Member extends BaseEntity {
    @Column(nullable = false)
    private String email;
    @Column(nullable = false)
    private String password;
    @Enumerated(EnumType.STRING)
    private Role role;
    @Column(columnDefinition = "integer default 0")
    private Integer point;
    private String kakaoAccessToken;

    protected Member() {
    }

    public Member(String email, String password, Role role) {
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public Integer subtractPoint(Integer point) {
        if (this.point < point) {
            throw new NotEnoughPointException();
        }
        this.point -= point;
        return point;
    }

    public void savePoints(Integer point) {
        this.point += point;
    }

    public void saveKakaoToken(String accessToken) {
        this.kakaoAccessToken = accessToken;
    }

    public String getEmail() {
        return email;
    }

    public Integer getPoint() {
        return point;
    }

    public String getKakaoAccessToken() {
        return kakaoAccessToken;
    }

    @Override
    public String toString() {
        return "Member{" +
            "id=" + getId() +
            ", email='" + email + '\'' +
            ", password='" + password + '\'' +
            ", role=" + role +
            '}';
    }
}
