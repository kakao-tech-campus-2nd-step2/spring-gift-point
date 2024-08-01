package gift.dto.member;

import gift.model.RegisterType;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "회원 수정 응답 데이터")
public record MemberEditResponse(
    @Schema(description = "회원 ID", example = "1")
    Long id,

    @Schema(description = "회원 이메일", example = "admin@kakao.com")
    String email,

    @Schema(description = "JWT 토큰", example = "abcde123456...")
    String token,

    @Schema(description = "회원 등록 유형", example = "DEFAULT")
    RegisterType registerType
) {

}
