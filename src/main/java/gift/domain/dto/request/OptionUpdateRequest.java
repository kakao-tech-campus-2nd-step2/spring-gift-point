package gift.domain.dto.request;

import gift.domain.annotation.RestrictedSpecialChars;
import gift.global.WebConfig.Constants.Domain;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.Range;

public record OptionUpdateRequest(
    @NotNull
    @Size(min = Domain.Option.NAME_LENGTH_MIN, max = Domain.Option.NAME_LENGTH_MAX)
    @RestrictedSpecialChars
    String name,
    @NotNull
    String action,
    @NotNull
    @Range(min = Domain.Option.QUANTITY_RANGE_MIN, max = Domain.Option.QUANTITY_RANGE_MAX)
    Integer quantity
) {

}
