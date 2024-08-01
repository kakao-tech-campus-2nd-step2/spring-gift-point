package gift.model.user;

import gift.common.enums.LoginType;
import gift.common.enums.Role;
import gift.exception.*;
import gift.model.wish.Wish;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

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

    private int point;

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

    public LoginType getLoginType() {
        return loginType;
    }

    public String getName() {
        return name;
    }

    public int getPoint() {
        return point;
    }

    public Role getRole() {
        return role;
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

    public void isAdmin(){
        if(this.role != Role.ADMIN){
            throw new NotAdminException("Forbidden");
        }
    }

    public void subtractPoint(int point){
        if(this.point < point){
            throw new LackPointException("포인트가 부족합니다.");
        }
        this.point -= point;
    }

    public void addPoint(int depositPoint){
        this.point += depositPoint;
    }

}

