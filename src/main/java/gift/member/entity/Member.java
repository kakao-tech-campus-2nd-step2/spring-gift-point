package gift.member.entity;

import gift.member.entity.embedded.Point;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Member {

    @Id
    private String email;

    @Column(nullable = false)
    private String password;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "point", nullable = false))
    private Point point;

    protected Member() {
    }

    public Member(String email, String password) {
        this.email = email;
        this.password = password;
        this.point = new Point();
    }

    public String getEmail() {
        return email;
    }

    public long getPoint() {
        return point.getValue();
    }

    public boolean isSamePassword(Member member) {
        return this.password.equals(member.password);
    }

    public void subtractPoint(long point) {
        this.point.subtract(point);
    }

    public void chargePoint(long point) {
        this.point.charge(point);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Member member) {
            return this.email.equals(member.email);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return this.email.hashCode();
    }
}
