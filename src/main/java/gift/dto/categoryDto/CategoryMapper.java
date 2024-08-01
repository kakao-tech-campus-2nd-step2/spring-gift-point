package gift.dto.categoryDto;

import gift.model.product.Category;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {
    public CategoryResponseDto toCategoryResponseDto(Category category) {
        return new CategoryResponseDto(category.getId(), category.getCategoryName(), category.getColor(), category.getImageUrl(), category.getDescription());
    }
}
