package gift.dto.member;

import gift.model.RegisterType;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "회원 로그인/회원가입 응답 데이터")
public record MemberAuthResponse(

    @Schema(description = "회원 이메일", example = "admin@kakao.com")
    String email,

    @Schema(description = "JWT 토큰", example = "abcde123456...")
    String token
) {

}
