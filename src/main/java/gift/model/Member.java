package gift.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.util.Base64;

@Entity
@Table(name = "member")
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "이메일은 1글자 이상 입력하세요")
    @Email(message = "이메일 형식을 지켜야 합니다")
    @Column(unique = true, nullable = false)
    private String email;

    @NotBlank(message = "비밀번호는 1글자 이상 입력하세요")
    @Column(nullable = false)
    private String password;

    protected Member() {}

    public Member(Long id, String email, String password) {
        this.id = id;
        this.email = email;
        this.password = password;
    }

    public static Member createWithEncodedPassword(Long id, String email, String rawPassword) {
        String encodedPassword = Base64.getEncoder().encodeToString(rawPassword.getBytes());
        return new Member(id, email, encodedPassword);
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
}
