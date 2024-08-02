package gift.response.category;

import java.util.List;

public record CategoryListResponse(
    List<CategoryResponse> contents
) {

}
