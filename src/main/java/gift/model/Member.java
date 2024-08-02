package gift.model;

import gift.exception.GiftOrderException;
import gift.exception.InvalidLoginInfoException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(name = "member")
@SQLDelete(sql = "update member set deleted = true where id = ?")
@SQLRestriction("deleted is false")
public class Member extends BaseEntity {
    @NotNull
    @Column(name = "email", unique = true)
    private String email;
    @NotNull
    @Column(name = "password")
    private String password;
    @NotNull
    @Column(name = "point")
    private Integer point = 0;
    @NotNull
    @Column(name = "deleted")
    private Boolean deleted = Boolean.FALSE;

    protected Member() {
    }

    public Member(String email, OauthType oauthType) {
        this.email = email;
        this.password = oauthType.name();
    }

    public Member(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public Integer getPoint() {
        return point;
    }

    public void passwordCheck(String inputPassword) {
        if (!password.equals(inputPassword)) {
            throw new InvalidLoginInfoException("로그인 정보가 유효하지 않습니다.");
        }
    }

    public void addPoint(Integer newPoint) {
        this.point = point + newPoint;
    }

    public void subtractPoint(Integer usedPoint) {
        if (point < usedPoint) {
            throw new GiftOrderException("사용가능한 포인트보다 더 많은 포인트를 사용할 수 없습니다.");
        }
        this.point = point - usedPoint;
    }
}
