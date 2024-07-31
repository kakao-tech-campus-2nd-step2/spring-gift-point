package gift.dto;

import gift.vo.Member;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "로그인 DTO")
public record LoginDto(

        @Schema(description = "회원 이메일")
        String email,

        @Schema(description = "회원 비밀번호")
        String password
) {
    public Member toUser() {
        return new Member(email, password);
    }
}
