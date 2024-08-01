package gift.model;

import gift.common.enums.Role;
import gift.common.enums.SocialType;
import gift.common.exception.ErrorCode;
import gift.common.exception.UserException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;

@Entity(name = "users")
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String password;

    @NotNull
    @Column(unique = true)
    private String email;

    @NotNull
    private String name;

    @Enumerated(EnumType.STRING)
    @NotNull
    private SocialType socialType;

    @Enumerated(EnumType.STRING)
    @NotNull
    private Role role;

    @NotNull
    private int point;

    protected User() {
    }

    public User(String password, String email, String name, SocialType socialType) {
        this.password = password;
        this.email = email;
        this.name = name;
        this.socialType = socialType;
        this.role = Role.USER;
        this.point = 0;
    }

    public Long getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public SocialType getSocialType() {
        return socialType;
    }

    public String getName() {
        return name;
    }

    public Role getRole() {
        return role;
    }

    public int getPoint() {
        return point;
    }

    public boolean checkSocialType(SocialType socialType) {
        return this.socialType == socialType;
    }

    public void subtractPoint(int point) {
        if (this.point < point) {
            throw new UserException(ErrorCode.LACK_POINT);
        }
        this.point -= point;
    }
}
