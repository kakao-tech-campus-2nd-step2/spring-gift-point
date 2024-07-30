package gift.classes.RequestState;

import gift.dto.CategoryDto;
import java.util.List;

public class CategoryRequestStateDTO extends RequestStateDTO {

    private final List<CategoryDto> categories;

    public CategoryRequestStateDTO(RequestStatus requestStatus, String details,
        List<CategoryDto> categories) {
        super(requestStatus, details);
        this.categories = categories;
    }

    public List<CategoryDto> getCategories() {
        return categories;
    }

}
