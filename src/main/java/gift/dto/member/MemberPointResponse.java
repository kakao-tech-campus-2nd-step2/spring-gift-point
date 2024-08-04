package gift.dto.member;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "회원 개별 포인트 응답 데이터")
public record MemberPointResponse(

    @Schema(description = "잔여 포인트", example = "10000")
    int amount
) {

}
