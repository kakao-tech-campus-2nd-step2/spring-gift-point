package gift.model;

import gift.common.enums.Role;
import gift.common.enums.SocialLoginType;
import gift.common.exception.AuthorizationException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import java.time.LocalDateTime;

@Entity
public class Member extends BasicEntity {

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SocialLoginType loginType;

    protected Member() {}

    public Member(Long id, String email, String password, Role role, LocalDateTime createdAt, LocalDateTime updatedAt) {
        super(id, createdAt, updatedAt);
        this.email = email;
        this.password = password;
        this.role = role;
        this.loginType = SocialLoginType.DEFAULT;
    }

    public Member(String email, String password, String name, Role role) {
        this.email = email;
        this.password = password;
        this.role = role;
        this.name = name;
        this.loginType = SocialLoginType.DEFAULT;
    }

    public Member(String email, String password, String name, Role role, SocialLoginType loginType) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.role = role;
        this.loginType = loginType;
    }

    public void updateMember(String email, String password, Role role) {
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public void checkLoginType(SocialLoginType loginType) {
        if (this.loginType != loginType) {
            throw new AuthorizationException("잘못된 접근입니다.");
        }
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public Role getRole() {
        return role;
    }

    public String getName() {
        return name;
    }

    public SocialLoginType getLoginType() {
        return loginType;
    }
}