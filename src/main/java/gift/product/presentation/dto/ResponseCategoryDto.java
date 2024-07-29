package gift.product.presentation.dto;

import gift.product.business.dto.CategoryDto;
import java.util.List;

public record ResponseCategoryDto(Long id, String name) {


    public static ResponseCategoryDto from(CategoryDto categoryDto) {
        return new ResponseCategoryDto(
            categoryDto.id(),
            categoryDto.name()
        );
    }

    public static List<ResponseCategoryDto> of(List<CategoryDto> categoryDtos) {
        return categoryDtos.stream()
            .map(ResponseCategoryDto::from)
            .toList();
    }
}
