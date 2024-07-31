package gift.api.option.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import gift.api.option.domain.Option;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record OptionResponse(
    Long id,
    String name,
    Integer quantity
) {
    public static OptionResponse of(Option option) {
        return new OptionResponse(option.getId(),
                                option.getName(),
                                option.getQuantity());
    }
}
