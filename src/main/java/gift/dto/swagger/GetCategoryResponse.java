package gift.dto.swagger;

import gift.dto.betweenClient.category.CategoryDTO;
import java.util.List;

public record GetCategoryResponse(List<CategoryDTO> categories){ }
