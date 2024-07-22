package gift.product.application.dto.response;

import gift.product.service.dto.CategoryPageInfo;
import java.util.List;

public record CategoryPageResponse(
        List<CategoryResponse> categories,
        long totalElements,
        int totalPages,
        int currentPage
) {
    public static CategoryPageResponse from(CategoryPageInfo categoryPageInfo) {
        var categories = categoryPageInfo.categories().stream()
                .map(CategoryResponse::from)
                .toList();
        return new CategoryPageResponse(
                categories,
                categoryPageInfo.totalElements(),
                categoryPageInfo.totalPages(),
                categoryPageInfo.currentPage()
        );
    }
}
