package gift.dto.member;

import static gift.util.constants.GeneralConstants.REQUIRED_FIELD_MISSING;

import gift.model.RegisterType;
import jakarta.validation.constraints.NotNull;

public record MemberEditRequest(
    Long id,

    @NotNull(message = REQUIRED_FIELD_MISSING)
    String email,

    @NotNull(message = REQUIRED_FIELD_MISSING)
    String password,

    @NotNull(message = REQUIRED_FIELD_MISSING)
    RegisterType registerType
) {

}
