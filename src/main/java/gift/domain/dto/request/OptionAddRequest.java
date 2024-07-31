package gift.domain.dto.request;

import gift.domain.annotation.RestrictedSpecialChars;
import gift.domain.entity.Option;
import gift.domain.entity.Product;
import gift.global.WebConfig.Constants.Domain;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;
import org.hibernate.validator.constraints.Range;

public record OptionAddRequest(
    @NotNull
    @Size(min = Domain.Option.NAME_LENGTH_MIN, max = Domain.Option.NAME_LENGTH_MAX, message = Domain.Option.NAME_LENGTH_INVALID_MSG)
    @RestrictedSpecialChars
    String name,
    @NotNull
    @Range(min = Domain.Option.QUANTITY_RANGE_MIN, max = Domain.Option.QUANTITY_RANGE_MAX, message = Domain.Option.QUANTITY_INVALID_MSG)
    Integer quantity
) {

    public static OptionAddRequest of(Option option) {
        return new OptionAddRequest(option.getName(), option.getQuantity());
    }

    public static List<OptionAddRequest> of(List<Option> options) {
        return options.stream()
            .map(OptionAddRequest::of)
            .toList();
    }

    public Option toEntity(Product product) {
        return new Option(product, name, quantity);
    }
}
