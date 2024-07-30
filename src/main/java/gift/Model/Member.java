package gift.Model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.beans.ConstructorProperties;

@Entity
@Table(name = "member")
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "멤버 ID(자동으로 설정)", defaultValue = "1")
    private Long id;

    @NotBlank(message = "입력은 공백일 수 없습니다.")
    @Email(message = "이메일 형식이어야 합니다.")
    @Column(name = "email", nullable = false, unique = true)
    @Schema(description = "멤버 이메일", defaultValue = "이메일")
    private String email;
    @NotBlank(message = "입력은 공백일 수 없습니다.")
    @Column(name = "password", nullable = false)
    @Schema(description = "멤버의 비밀번호", defaultValue = "비밀번호")
    private String password;

    @NotBlank
    @Column(name = "accessToken", nullable = false)
    @Schema(description = "멤버의 accessToken", defaultValue = "해당되는 accessToken")
    private String accessToken;

    protected Member(){

    }

    @ConstructorProperties({"id","email","password","accessToken"})
    public Member(Long id, String email, String password, String accessToken) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.accessToken = accessToken;
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

    public String getAccessToken() {
        return accessToken;
    }
}
