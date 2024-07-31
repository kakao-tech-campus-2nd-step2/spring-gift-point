package gift.option;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.util.List;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record OptionContents(
    List<OptionResponse> contents
) {

    public static OptionContents from(List<Option> options) {
        List<OptionResponse> optionContents = options.stream()
            .map(OptionResponse::from)
            .toList();

        return new OptionContents(
            optionContents
        );
    }
}
