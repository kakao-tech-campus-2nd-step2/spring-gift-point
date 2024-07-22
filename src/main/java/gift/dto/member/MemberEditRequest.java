package gift.dto.member;

import static gift.util.constants.GeneralConstants.REQUIRED_FIELD_MISSING;

import jakarta.validation.constraints.NotNull;

public record MemberEditRequest(
    Long id,

    @NotNull(message = REQUIRED_FIELD_MISSING)
    String email,

    String password
) {

}
