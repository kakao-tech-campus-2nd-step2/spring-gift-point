package gift.model;

import static gift.util.constants.MemberConstants.INVALID_EMAIL;
import static gift.util.constants.MemberConstants.INVALID_PASSWORD;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "member")
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 255)
    @Email(message = INVALID_EMAIL)
    private String email;

    @Column(nullable = false, length = 255)
    @Size(min = 4, message = INVALID_PASSWORD)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "register_type", nullable = false)
    private RegisterType registerType;

    protected Member() {
    }

    public Member(Long id, String email, String password, RegisterType registerType) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.registerType = registerType;
    }

    public Member(String email, String password, RegisterType registerType) {
        this.email = email;
        this.password = password;
        this.registerType = registerType;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public RegisterType getRegisterType() {
        return registerType;
    }

    public void update(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public boolean isEmailMatching(String email) {
        return this.email.equals(email);
    }

    public boolean isPasswordMatching(String password) {
        return this.password.equals(password);
    }

    public boolean isIdMatching(Long id) {
        return this.id.equals(id);
    }

    public boolean isRegisterTypeDefault() {
        return this.registerType == RegisterType.DEFAULT;
    }
}
