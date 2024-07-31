package gift.response;

import java.util.List;

public record CategoryListResponse(
    List<CategoryResponse> contents
) {

}
