package gift.dto.category;

import gift.model.Category;

import java.util.List;

public record CategoriesResponseDTO(List<Category> categories) {
}
