package gift.member.entity;

import gift.member.dto.MemberDto;
import gift.member.exception.InsufficientPointException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Table(name = "member")
public class Member {

    @Id
    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "point", nullable = false)
    @ColumnDefault("0")
    private int point;

    protected Member() {

    }

    public Member(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public void changePassword(String newPassword) {
        this.password = newPassword;
    }

    public void addPoint(int point) {
        this.point += point;
    }

    public void subtractPoint(int point) {
        if (this.point < point) {
            throw new InsufficientPointException(this.point);
        }
        this.point -= point;
    }

    public MemberDto toDto() {
        return new MemberDto(email, password, point);
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public int getPoint() {
        return point;
    }
}
