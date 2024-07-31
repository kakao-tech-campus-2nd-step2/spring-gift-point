package gift.domain.entity;

import gift.domain.exception.conflict.CategoryHasProductsException;
import jakarta.persistence.PreRemove;

public class CategoryListener {

    @PreRemove
    public void preRemove(Category category) {
        if (category.getProducts() != null && !category.getProducts().isEmpty()) {
            throw new CategoryHasProductsException();
        }
    }
}
