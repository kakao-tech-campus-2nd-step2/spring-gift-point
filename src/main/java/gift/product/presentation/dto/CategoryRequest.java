package gift.product.presentation.dto;

import gift.product.business.dto.CategoryIn;
import jakarta.validation.constraints.NotBlank;

public class CategoryRequest {
    public record Create(
        @NotBlank
        String name,
        String description,
        String color,
        String imageUrl
    ) {

        public CategoryIn.Create toCategoryInCreate() {
            return new CategoryIn.Create(name(), description(), color(), imageUrl());
        }
    }

    public record Update(
        @NotBlank
        String name,
        String description,
        String color,
        String imageUrl
    ) {

        public CategoryIn.Update toCategoryInUpdate(Long id) {
            return new CategoryIn.Update(id, name(), description(), color(), imageUrl());
        }
    }

}
