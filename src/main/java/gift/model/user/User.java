package gift.model.user;

import gift.common.enums.LoginType;
import gift.common.enums.Role;
import gift.exception.InvalidUserException;
import gift.model.option.Option;
import gift.model.wish.Wish;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.springframework.boot.context.properties.bind.DefaultValue;

import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Wish> wishes = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private Role role;

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
        this.role = Role.USER;
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

