package gift.web.dto.response.category;

import java.util.List;

public class ReadAllCategoriesResponse {

    private final List<ReadCategoryResponse> categories;

    public ReadAllCategoriesResponse(List<ReadCategoryResponse> categories) {
        this.categories = categories;
    }

    public List<ReadCategoryResponse> getCategories() {
        return categories;
    }
}
