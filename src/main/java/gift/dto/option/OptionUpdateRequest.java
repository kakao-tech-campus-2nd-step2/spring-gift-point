package gift.dto.option;

import static gift.util.constants.GeneralConstants.REQUIRED_FIELD_MISSING;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "옵션 수정 요청 데이터")
public record OptionUpdateRequest(
    @NotNull(message = REQUIRED_FIELD_MISSING)
    @Schema(description = "옵션 이름", example = "색상: 빨간색")
    String name,

    @NotNull(message = REQUIRED_FIELD_MISSING)
    @Schema(description = "옵션 수량", example = "100")
    Integer quantity
) {

}
