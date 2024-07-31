package gift.order.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import gift.product.option.entity.Option;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record TemplateArgs(
    String message,
    String productImage,
    String productName
) {

    public static TemplateArgs of(String message, Option option) {
        return new TemplateArgs(message, option.getProduct().getImageUrl(),
            option.getProduct().getName());
    }

}
