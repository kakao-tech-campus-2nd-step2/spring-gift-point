package gift.product.presentation.dto;

import gift.product.business.dto.CategoryRegisterDto;
import gift.product.business.dto.CategoryUpdateDto;
import jakarta.validation.constraints.NotBlank;

public record RequestCategoryDto(
    @NotBlank
    String name
) {

    public CategoryRegisterDto toCategoryRegisterDto() {
        return new CategoryRegisterDto(
            name()
        );
    }

    public CategoryUpdateDto toCategoryUpdateDto(Long id) {
        return new CategoryUpdateDto(
            id,
            name()
        );
    }
}
