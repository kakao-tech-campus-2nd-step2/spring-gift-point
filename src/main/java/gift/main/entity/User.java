package gift.main.entity;

import gift.main.Exception.CustomException;
import gift.main.Exception.ErrorCode;
import gift.main.dto.PointResponse;
import gift.main.dto.UserJoinRequest;
import jakarta.persistence.*;

import java.util.Objects;


@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;


    @Column
    private int point;

    @Enumerated(EnumType.STRING)
    private Role role;

    public User() {

    }

    public User(long id, String name, String email, String password, Role role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
        this.point = 0;
    }

    public User(String name, String email, String password, Role role) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
        this.point = 0;
    }

    public User(long id, String name, String email, String password, String role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = Role.valueOf(role.toUpperCase());
        this.point = 0;
    }

    public User(UserJoinRequest userJoinRequest) {
        this.name = "유저";
        this.email = userJoinRequest.email();
        this.password = userJoinRequest.password();
        this.role = Role.USER;
        this.point = 0;
    }

    public User(String name, String email, String password, String role) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = Role.valueOf(role.toUpperCase());
        this.point = 0;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public Role getRole() {
        return role;
    }

    public int getPoint() {
        return point;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public void updatePoint(int point) {
        this.point += point;
    }

    public void checkUsingPoint(int point) {
        if (this.point < point) {
            throw new CustomException(ErrorCode.NOT_ENOUGH_POINT);
        }
    }
}
