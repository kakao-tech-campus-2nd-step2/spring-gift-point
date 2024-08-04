package gift.api.order.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import gift.api.member.domain.Member;
import gift.api.option.domain.Option;
import gift.api.order.domain.Order;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record OrderRequest(
    Long optionId,
    Integer quantity,
    String message,
    Integer point
) {

    public Order toEntity(Member member, Option option) {
        return new Order(member, option, quantity, message);
    }
}
