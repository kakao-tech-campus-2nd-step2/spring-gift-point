package gift.dto.member;

import static gift.util.constants.GeneralConstants.REQUIRED_FIELD_MISSING;

import gift.model.RegisterType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "회원 등록 요청 데이터")
public record MemberRegisterRequest(
    @NotNull(message = REQUIRED_FIELD_MISSING)
    @Schema(description = "회원 이메일", example = "admin@kakao.com")
    String email,

    @NotNull(message = REQUIRED_FIELD_MISSING)
    @Schema(description = "회원 비밀번호", example = "admin")
    String password,

    @NotNull(message = REQUIRED_FIELD_MISSING)
    @Schema(description = "회원 등록 유형", example = "DEFAULT")
    RegisterType registerType
) {

}
