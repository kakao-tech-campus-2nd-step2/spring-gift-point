package gift.dto.member;

import static gift.util.constants.GeneralConstants.REQUIRED_FIELD_MISSING;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Schema(description = "회원 수정 요청 데이터")
public record MemberPointRequest(
    @NotNull(message = REQUIRED_FIELD_MISSING)
    @Positive(message = "양수만 입력할 수 있습니다.")
    @Schema(description = "추가할 포인트", example = "5000")
    Integer amount
) {

}
