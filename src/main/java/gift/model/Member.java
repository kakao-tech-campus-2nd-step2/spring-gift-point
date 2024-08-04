package gift.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "member")
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false)
    private String password;
    @Column
    private String activeToken;
    @Column(nullable = false)
    private int point;

    protected Member() {
    }

    public Member(Member member, String activeToken) {
        this.id = member.id;
        this.email = member.email;
        this.password = member.password;
        this.activeToken = activeToken;
        this.point = member.point;
    }

    public Member(Member member) {
        this.id = member.id;
        this.email = member.email;
        this.password = member.password;
        this.point = member.point;
    }

    public Member(String email, String password) {
        this.email = email;
        this.password = password;
        this.point = 0;
    }

    public Member(String email, String password, String activeToken) {
        this.email = email;
        this.password = password;
        this.activeToken = activeToken;
        this.point = 0;
    }

    public Member(Long id, String email, String password, String activeToken, int point) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.activeToken = activeToken;
        this.point = point;
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

    public String getActiveToken() {
        return activeToken;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }
}
