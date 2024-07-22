package gift.product.application.dto.request;

import gift.product.service.command.CategoryCommand;
import jakarta.validation.constraints.NotNull;

public record CategoryRequest(
        @NotNull
        String name,
        @NotNull
        String color,
        String imgUrl,
        String description
) {
    public CategoryCommand toCategoryParam() {
        return new CategoryCommand(name, color, imgUrl, description);
    }
}
