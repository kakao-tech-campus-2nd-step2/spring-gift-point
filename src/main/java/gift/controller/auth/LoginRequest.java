package gift.controller.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginRequest(
    @NotBlank(message = "이메일을 입력하세요.")
    @Size(max = 30, message = "이메일은 최대 30자 이내입니다.")
    String email,
    @Size(min = 8, max = 20, message = "비밀번호의 길이는 8자 이상, 20자 이하 이내입니다.")
    String rawPassword) { }