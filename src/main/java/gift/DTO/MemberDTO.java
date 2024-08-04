package gift.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.beans.ConstructorProperties;

public class MemberDTO {
    @Schema(description = "멤버 ID(자동으로 설정)", defaultValue = "1")
    private Long id;

    @NotBlank(message = "입력은 공백일 수 없습니다.")
    @Email(message = "이메일 형식이어야 합니다.")
    @Schema(description = "멤버 이메일", defaultValue = "이메일")
    private String email;
    @NotBlank(message = "입력은 공백일 수 없습니다.")
    @Schema(description = "멤버의 비밀번호", defaultValue = "비밀번호")
    private String password;
    @Schema(description = "멤버의 accessToken", defaultValue = "해당되는 accessToken")
    private String accessToken;
    @Schema(description = "멤버의 point", defaultValue = "1000")
    private int point;


    protected MemberDTO(){

    }

    @ConstructorProperties({"id","email","password","accessToken","point"})
    public MemberDTO(Long id, String email, String password, String accessToken, int point) {

        this.id = id;
        this.email = email;
        this.password = password;
        this.accessToken = accessToken;
        this.point = point;

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

    public int getPoint() {
        return point;
    }

}
