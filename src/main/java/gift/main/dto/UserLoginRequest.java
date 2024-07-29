package gift.main.dto;


import jakarta.validation.constraints.NotBlank;

public record UserLoginRequest(
        @NotBlank(message = "이메일을 적어주세요.")
        String email,
        @NotBlank(message = "패스워드 입력해주세요.")
        String password) {

}
