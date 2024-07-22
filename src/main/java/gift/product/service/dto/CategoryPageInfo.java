package gift.product.service.dto;

import gift.product.domain.Category;
import java.util.List;
import org.springframework.data.domain.Page;

public record CategoryPageInfo(
        List<CategoryInfo> categories,
        long totalElements,
        int totalPages,
        int currentPage
) {
    public static CategoryPageInfo from(Page<Category> categoryPages) {
        var categories = categoryPages.stream()
                .map(CategoryInfo::from)
                .toList();

        return new CategoryPageInfo(
                categories,
                categoryPages.getTotalElements(),
                categoryPages.getTotalPages(),
                categoryPages.getNumber()
        );
    }
}
