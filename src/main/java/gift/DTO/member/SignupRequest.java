package gift.DTO.member;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class SignupRequest {

    @Email
    private String email;

    @NotBlank
    private String password;

    private String confirmPassword;

    private Long kakaoId;

    public SignupRequest() {
    }

    public SignupRequest(Long kakaoId) {
        this.email = kakaoId + "@kakao.com";
        this.password = "password";
        this.confirmPassword = this.password;
        this.kakaoId = kakaoId;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public Long getKakaoId() {
        return kakaoId;
    }
}
