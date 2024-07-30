package gift.dto;

import gift.vo.Member;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "회원가입 요청 DTO")
public record JoinMemberDto(

        @Schema(description = "회원 이메일", example = "example@example.com")
        String email,

        @Schema(description = "회원 비밀번호", example = "password123")
        String password
){
    public Member toMember() {
        return new Member(email, password);
    }
}
