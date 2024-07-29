package gift.controller.member;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class SignUpRequest {

    @NotBlank(message = "이메일은 필수 입력 항목입니다.")
    @Size(max = 25, message = "이메일은 최대 30자 이내입니다.")
    @Pattern(regexp = "^[\\w\\d+-_.]+@[\\w\\d]+.[\\w]+$", message = "적절한 이메일 형식이 아닙니다")
    private String email;
    @Size(min = 8, max = 20, message = "비밀번호의 길이는 8자 이상, 20자 이하 이내입니다.")
    private String password;
    @NotBlank(message = "닉네임은 필수 입력 항목입니다.")
    @Size(max = 15, message = "닉네임은 최대 15자까지 가능합니다.")
    private final String nickName;

    public SignUpRequest(String email, String password, String nickName) {
        this.email = email;
        this.password = password;
        this.nickName = nickName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickName() {
        return nickName;
    }
}
