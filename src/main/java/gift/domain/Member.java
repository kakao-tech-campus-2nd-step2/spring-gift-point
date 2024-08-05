package gift.domain;

import jakarta.persistence.*;

@Entity
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "point", nullable = false)
    private int point;

    protected Member() {}

    public Member(Long id, String email, String password) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.point = 0;
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

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public void addPoint(int point) {
        this.point += point;
    }

    public void usePoint(int price) {
        if (this.point >= price) {
            this.point -= price;
        } else {
            this.point = 0;
        }
    }
}
