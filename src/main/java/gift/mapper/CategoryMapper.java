package gift.mapper;

import gift.domain.category.Category;
import gift.web.dto.category.CategoryRequestDto;
import gift.web.dto.category.CategoryResponseDto;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {
    public CategoryResponseDto toDto(Category category) {
        return new CategoryResponseDto(category.getId(),
            category.getName(),
            category.getColor(),
            category.getImageUrl(),
            category.getDescription());
    }

    public Category toEntity(CategoryRequestDto categoryRequestDto) {
        return new Category(categoryRequestDto.name(),
            categoryRequestDto.color(),
            categoryRequestDto.description(),
            categoryRequestDto.imageUrl());
    }
}