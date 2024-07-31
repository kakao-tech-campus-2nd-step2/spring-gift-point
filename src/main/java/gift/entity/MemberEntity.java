package gift.entity;

import gift.domain.Member;
import gift.util.LoginType;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "members")
public class MemberEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LoginType loginType;

    public MemberEntity() {

    }

    public MemberEntity(String email, String password, LoginType loginType) {
        this.email = email;
        this.password = password;
        this.loginType = loginType;
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

    public LoginType getLoginType() {
        return loginType;
    }

}
