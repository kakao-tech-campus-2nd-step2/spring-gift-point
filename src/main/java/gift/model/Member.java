package gift.model;

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

    public void passwordCheck(String inputPassword) {
        if (!password.equals(inputPassword)) {
            throw new InvalidLoginInfoException("로그인 정보가 유효하지 않습니다.");
        }
    }
}
