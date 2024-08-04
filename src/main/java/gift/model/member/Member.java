package gift.model.member;

import gift.model.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Entity
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(unique = true)
    private String email;

    @NotNull
    private String password;

    @NotNull
    private String name;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Role role;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Provider provider;

    protected Member() {
    }

    public Member(Long id, String email, String password, String name, Role role,
        Provider provider) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
        this.role = role;
        this.provider = provider;
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

    public String getName() {
        return name;
    }

    public Role getRole() {
        return role;
    }

    public void changeProvider(Provider provider) {
        this.provider = provider;
    }

    // 비밀번호 검증 메서드
    public boolean verifyPassword(String originalPassword) {
        String hashedPassword = PasswordUtil.hashPasswordWithSalt(originalPassword);
        return hashedPassword.equals(this.password);
    }

    public static Member create(Long id, String email, String password, String name, Role role) {
        return new Member(id, email, PasswordUtil.hashPasswordWithSalt(password), name, role,
            Provider.ORIGIN);
    }

    public static Member create(Long id, String email, String password, String name, Role role,
        Provider provider) {
        return new Member(id, email, PasswordUtil.hashPasswordWithSalt(password), name, role,
            provider);
    }

}

class PasswordUtil {

    // 비밀번호와 salt를 함께 해싱하는 메서드
    public static String hashPasswordWithSalt(String password) {
        String salt = "secret";
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(salt.getBytes());
            byte[] hash = md.digest(password.getBytes());
            return bytesToHex(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    // 바이트 배열을 16진수 문자열로 변환하는 메서드
    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }


}
