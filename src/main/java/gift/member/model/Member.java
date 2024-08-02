package gift.member.model;

import gift.common.exception.MemberException;
import gift.common.model.BaseEntity;
import gift.member.MemberErrorCode;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;

@Entity
public class Member extends BaseEntity {

    @Column(name = "email", nullable = false, unique = true)
    private String email;
    @Column(name = "password", nullable = false)
    private String password;
    @Column(name = "name", length = 15)
    private String name;
    @Column(name = "role")
    private String role;
    @Column(name = "point")
    private Integer point;

    protected Member() {

    }

    public Member(String email, String password, String name, String role) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.role = role;
    }

    public Member(Long id, String email, String name, String role) {
        this.setId(id);
        this.email = email;
        this.name = name;
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public String getRole() {
        return role;
    }

    public Integer getPoint() {
        return point;
    }

    public boolean matchPassword(String password) {
        return this.password.equals(password);
    }

    public void usePoint(Integer point) throws MemberException {
        if (this.point < point) {
            throw new MemberException(MemberErrorCode.NOT_ENOUGH_POINT);
        }
    }

    public void accumulatePoint(int accumulatedPoint) {
        point = point + accumulatedPoint;
    }
}
