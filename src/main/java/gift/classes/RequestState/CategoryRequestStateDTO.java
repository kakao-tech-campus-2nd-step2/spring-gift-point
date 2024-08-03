package gift.classes.RequestState;

import gift.dto.CategoryDto;
import java.util.List;
import org.springframework.http.HttpStatus;

public class CategoryRequestStateDTO extends RequestStateDTO {

    private final List<CategoryDto> data;

    public CategoryRequestStateDTO(HttpStatus status, String message, List<CategoryDto> data) {
        super(status, message);
        this.data = data;
    }

    public List<CategoryDto> getData() {
        return data;
    }
}
