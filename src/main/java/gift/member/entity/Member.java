package gift.member.entity;

import gift.member.dto.MemberDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "member")
public class Member {

    @Id
    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "point")
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

    public MemberDto toDto() {
        return new MemberDto(email, password);
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
