package gift.domain.dto.request;

import gift.domain.entity.Member;
import gift.domain.entity.Option;
import gift.domain.entity.Order;
import gift.global.WebConfig.Constants.Domain;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Range;

public record OrderRequest(
    @NotNull
    Long optionId,
    @NotNull
    @Range(min = Domain.Option.QUANTITY_RANGE_MIN, max = Domain.Option.QUANTITY_RANGE_MAX)
    Integer quantity,
    @NotNull
    String message
) {

    public Order toEntity(Member member, Option option) {
        return new Order(member, option, quantity, message);
    }

}
