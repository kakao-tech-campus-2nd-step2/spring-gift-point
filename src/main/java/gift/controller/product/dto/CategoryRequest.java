package gift.controller.product.dto;

import gift.application.product.dto.CategoryCommand;
import jakarta.validation.constraints.NotBlank;

public class CategoryRequest {

    public record Register(
        @NotBlank
        String name,
        @NotBlank
        String color,
        String description,
        @NotBlank
        String imageUrl
    ) {

        public CategoryCommand.Register toCommand() {
            return new CategoryCommand.Register(name, color, description, imageUrl);
        }
    }

    public record Update(
        @NotBlank
        String name,
        @NotBlank
        String color,
        String description,
        @NotBlank
        String imageUrl
    ) {

        public CategoryCommand.Update toCommand() {
            return new CategoryCommand.Update(name, color, description, imageUrl);
        }
    }


}
