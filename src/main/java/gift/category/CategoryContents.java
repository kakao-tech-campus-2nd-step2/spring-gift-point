package gift.category;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.util.List;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record CategoryContents(
    List<CategoryResponse> contents
) {

    public static CategoryContents from(List<Category> categories) {
        List<CategoryResponse> categoryContents = categories.stream()
            .map(CategoryResponse::from)
            .toList();

        return new CategoryContents(
            categoryContents
        );
    }
}
