package gift.converter;

import gift.dto.CategoryDTO;
import gift.model.Category;

public class CategoryConverter {

    public static CategoryDTO convertToDTO(Category category) {
        return new CategoryDTO(category.getId(), category.getName());
    }

    public static Category convertToEntity(CategoryDTO categoryDTO) {
        return new Category(categoryDTO.getId(), categoryDTO.getName());
    }
}