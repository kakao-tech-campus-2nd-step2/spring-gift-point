package gift.dto;

import gift.domain.Member;
import gift.domain.Option;
import gift.domain.OrderOption;
import jakarta.validation.constraints.NotNull;
import java.sql.Timestamp;
import org.hibernate.validator.constraints.Range;

public record OrderOptionDTO(
    long id,

    @NotNull
    long optionId,

    @NotNull
    @Range(min = 1, max = 99999999)
    int quantity,

    Timestamp orderDateTime,

    @NotNull
    String message
) {

    public OrderOption toEntity(Option option, Member member) {
        return new OrderOption(option, quantity, message, member);
    }
}
