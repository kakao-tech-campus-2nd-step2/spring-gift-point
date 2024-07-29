package gift.dto.oauth;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "카카오 사용자 응답 데이터")
public record KakaoUserResponse(
    @Schema(description = "사용자 ID", example = "123456789")
    Long id,

    @Schema(description = "사용자 닉네임", example = "이경빈")
    String nickname,

    @Schema(description = "사용자 이메일", example = "cussle@kakao.com")
    String email
) {

}
