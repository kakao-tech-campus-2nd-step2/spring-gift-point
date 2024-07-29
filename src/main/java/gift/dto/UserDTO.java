package gift.dto;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record UserDTO(
        @NotNull(message = "email을 입력해주세요")
        @Pattern(regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$",
                message = "올바른 형식의 이메일을 입력해 주세요")
        String email,
        @NotNull(message = "비밀번호를 입력해주세요")
        String password) {}