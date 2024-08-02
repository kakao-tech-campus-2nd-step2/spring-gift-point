package gift.domain.entity;

import gift.domain.exception.ErrorCode;
import gift.domain.exception.ServerException;
import jakarta.persistence.PreRemove;

public class CategoryListener {

    @PreRemove
    public void preRemove(Category category) {
        if (category.getProducts() != null && !category.getProducts().isEmpty()) {
            throw new ServerException(ErrorCode.CATEGORY_HAS_PRODUCTS);
        }
    }
}
