package gift.domain;

import gift.BaseTimeEntity;
import gift.exception.order.OrderCustomException.InsufficientPointsException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import security.SHA256Util;

@Entity
@Table(name = "app_user")
@SQLDelete(sql = "UPDATE app_user SET deletion_date = CURRENT_TIMESTAMP WHERE id = ?")
@SQLRestriction("deletion_date IS NULL")
public class AppUser extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, length = 100)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private Role role = Role.USER;

    @Column(length = 255)
    private String salt;

    private String accessToken;

    @ColumnDefault("3000")
    private int point;

    public AppUser() {
    }

    public AppUser(String email, String password, Role role, String salt) {
        this.email = email;
        this.password = password;
        this.role = role;
        this.salt = salt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public int getPoint() {
        return point;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public void updatePassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public String getSalt() {
        return salt;
    }

    public boolean isPasswordCorrect(String inputPassword) {
        String hashedInputPassword = SHA256Util.encodePassword(inputPassword, this.salt);
        return this.password.equals(hashedInputPassword);
    }

    public boolean isAdmin() {
        return this.role == Role.ADMIN;
    }

    public void addPoint(int point) {
        this.point += point;
    }

    public void usePoint(int point) {
        if (this.point < point) {
            throw new InsufficientPointsException();
        }
        this.point -= point;
    }
}