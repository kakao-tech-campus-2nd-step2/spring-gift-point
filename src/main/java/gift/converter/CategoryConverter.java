package gift.converter;

import gift.dto.CategoryDTO;
import gift.model.Category;

public class CategoryConverter {

    public static CategoryDTO convertToDTO(Category category) {
        return new CategoryDTO(
            category.getId(),
            category.getName(),
            category.getColor(),
            category.getImageUrl(),
            category.getDescription()
        );
    }

    public static Category convertToEntity(CategoryDTO categoryDTO) {
        return new Category(
            categoryDTO.getId(),
            categoryDTO.getName(),
            categoryDTO.getColor(),
            categoryDTO.getImageUrl(),
            categoryDTO.getDescription()
        );
    }
}