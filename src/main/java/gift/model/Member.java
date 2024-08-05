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
    private int points;

    protected Member() {
    }

    public Member(Member member, String activeToken) {
        this.id = member.id;
        this.email = member.email;
        this.password = member.password;
        this.activeToken = activeToken;
        this.points = member.points;
    }

    public Member(Member member) {
        this.id = member.id;
        this.email = member.email;
        this.password = member.password;
        this.points = member.points;
    }

    public Member(String email, String password) {
        this.email = email;
        this.password = password;
        this.points = 0;
    }

    public Member(String email, String password, String activeToken) {
        this.email = email;
        this.password = password;
        this.activeToken = activeToken;
        this.points = 0;
    }

    public Member(Long id, String email, String password, String activeToken, int points) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.activeToken = activeToken;
        this.points = points;
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

    public int getPoints() {
        return points;
    }

    public void setPoints(int point) {
        this.points = points;
    }
}
