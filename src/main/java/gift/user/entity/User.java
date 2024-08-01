package gift.user.entity;

import gift.exception.CustomException;
import gift.exception.ErrorCode;
import gift.wish.entity.Wish;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Pattern;

@Entity
@Table(
    name = "users",
    uniqueConstraints = @UniqueConstraint(columnNames = {"email"}, name = "uk_users" )
)
public class User {

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^(.+)@(\\S+)$" );

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, columnDefinition = "boolean default false" )
    private Boolean isKakao;

    @Column(nullable = true, length = 100)
    private String accessToken;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<UserRole> userRoles;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private Set<Wish> wishes;

    public User(String email, String password, Boolean isKakao, Set<UserRole> userRoles) {
        validateEmail(email);
        this.email = email;
        this.password = password;
        this.isKakao = Objects.requireNonNullElse(isKakao, Boolean.FALSE);
        this.userRoles = userRoles;
    }

    protected User() {
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public Set<UserRole> getRoles() {
        return userRoles;
    }

    public Set<Wish> getWishes() {
        return wishes;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public void changePassword(String password) {
        this.password = password;
    }

    private void validateEmail(String email) {
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new CustomException(ErrorCode.INVALID_EMAIL);
        }
    }

}
