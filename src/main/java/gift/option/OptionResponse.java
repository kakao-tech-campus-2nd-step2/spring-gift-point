package gift.option;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record OptionResponse(
    Long id,
    String name,
    int quantity
) {

    public OptionResponse(Option option) {
        this(option.getId(), option.getName(), option.getQuantity());
    }

    public static OptionResponse from(Option option) {
        return new OptionResponse(
            option.getId(), option.getName(), option.getQuantity()
        );
    }
}
