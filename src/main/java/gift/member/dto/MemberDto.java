package gift.member.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

public record MemberDto(
        @NotEmpty(message = "반드시 값이 존재해야 합니다.")
        @Email(message = "이메일 형식에 맞춰 작성해야 합니다.")
        String email,
        @NotEmpty(message = "반드시 값이 존재해야 합니다.")
        String password) { }
