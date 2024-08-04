package gift.member.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import org.hibernate.annotations.ColumnDefault;

@Entity
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "point", nullable = false)
    @ColumnDefault("10000")
    private int point;

    protected Member() {}

    public Member(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
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

    public String getName() {
        return name;
    }

    public int getPoint() {
        return point;
    }

    public void initPoint(int point) {
        this.point = point;
    }

    public void addPoint(int productPrice) {
        this.point += productPrice * 0.03;
    }

    public void usePoint(int point) {
        this.point -= point;
    }

}
