package gift.dto.request;

import jakarta.validation.constraints.NotEmpty;
import org.hibernate.validator.constraints.Length;

public class CategoryRequest {

    @Length(max = 15)
    @NotEmpty
    private String name;

    public String getName() {
        return name;
    }

    public CategoryRequest() {
    }

    public CategoryRequest(String name) {
        this.name = name;
    }

}
