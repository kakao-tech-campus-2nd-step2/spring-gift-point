package gift.product.category.entity;

import gift.exception.CustomException;
import gift.exception.ErrorCode;
import java.util.ArrayList;
import java.util.List;

public class Categories {

    private final List<Category> categories;

    public Categories(List<Category> categories) {
        this.categories = new ArrayList<>(categories);
    }

    public void validate(Category category) {
        if (categories.stream().anyMatch(it -> it.getName().equals(category.getName()))) {
            throw new CustomException(ErrorCode.CATEGORY_NOT_FOUND);
        }
    }

}
