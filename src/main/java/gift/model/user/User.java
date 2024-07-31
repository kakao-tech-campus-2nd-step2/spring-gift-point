package gift.model.user;

import gift.common.enums.LoginType;
import gift.exception.InvalidUserException;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    @NotNull
    private String email;

    @NotNull
    private String password;

    @NotNull
    private String name;

    @Enumerated(EnumType.STRING)
    @NotNull
    private LoginType loginType;


    public User() {
    }

    public User(String email, String password, String name, LoginType loginType) {
        this.email = email;
        this.password = password;
        this.name = name;
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

    public String getName() {
        return name;
    }

    public void checkLoginType(LoginType loginType) {
        if (this.loginType != loginType) {
            throw new InvalidUserException("카카오 서비스를 이용할 수 없는 유저입니다.");
        }
    }

    public void isDefaultLogin() {
        if (this.loginType != LoginType.DEFAULT) {
            throw new InvalidUserException("일반 로그인을 할 수 없습니다.(소셜 로그인 유저)");
        }
    }

}

