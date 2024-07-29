package gift.user.infrastructure.persistence.entity;

import gift.core.domain.user.UserAccount;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "user_account")
public class UserAccountEntity {
    @Id
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    protected UserAccountEntity() {
    }

    protected UserAccountEntity(Long userId, String email, String password) {
        this.userId = userId;
        this.email = email;
        this.password = password;
    }

    public static UserAccountEntity of(Long userId, UserAccount userAccount) {
        return new UserAccountEntity(userId, userAccount.principal(), userAccount.credentials());
    }

    public Long getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
