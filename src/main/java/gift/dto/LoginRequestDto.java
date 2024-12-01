package gift.dto;

import gift.vo.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "로그인 요청 DTO")
public record LoginRequestDto(

        @NotBlank
        @Schema(description = "회원 이메일")
        String email,

        @NotBlank
        @Schema(description = "회원 비밀번호")
        String password
) {
    public Member toUser() {
        return new Member(email, password);
    }
}
