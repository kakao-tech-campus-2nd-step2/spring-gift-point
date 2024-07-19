package gift.user.model.dto;

import gift.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import security.SHA256Util;

@Entity
@Table(name = "app_user")
public class AppUser extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, length = 100)
    private String password;

    @Column(name = "is_active", nullable = false, columnDefinition = "boolean default true")
    private Boolean isActive = true;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private Role role = Role.USER;

    @Column(length = 255)
    private String salt;

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

    public void updatePassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public String getSalt() {
        return salt;
    }

    public Boolean isActive() {
        return isActive;
    }

    public void inactive() {
        this.isActive = false;
    }

    public boolean isPasswordCorrect(String inputPassword) {
        String hashedInputPassword = SHA256Util.encodePassword(inputPassword, this.salt);
        return this.password.equals(hashedInputPassword);
    }

    public boolean isAdmin() {
        return this.role == Role.ADMIN;
    }
}