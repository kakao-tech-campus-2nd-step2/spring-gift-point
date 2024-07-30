package gift.model;

import jakarta.persistence.*;

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

    protected Member() {
    }

    public Member(Member member, String activeToken) {
        this.id = member.id;
        this.email = member.email;
        this.password = member.password;
        this.activeToken = activeToken;
    }

    public Member(Member member) {
        this.id = member.id;
        this.email = member.email;
        this.password = member.password;
    }

    public Member(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public Member(String email, String password, String activeToken) {
        this.email = email;
        this.password = password;
        this.activeToken = activeToken;
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
}
