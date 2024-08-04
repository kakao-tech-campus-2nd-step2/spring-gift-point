package gift.domain.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "회원 응답 Dto")
public record MemberResponse(
    @Schema(description = "Id 식별자")
    Long id,
    @Schema(description = "회원 이메일")
    String email,
    @Schema(description = "회원 패스워드")
    String password,
    @Schema(description = "멤버 포인트")
    int point) {

}
