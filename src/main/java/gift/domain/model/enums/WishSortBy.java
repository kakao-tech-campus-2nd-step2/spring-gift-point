package gift.domain.model.enums;

import org.springframework.data.domain.Sort;

public enum WishSortBy {
    ID_ASC, ID_DESC, PRODUCT_NAME_ASC, PRODUCT_NAME_DESC, PRODUCT_PRICE_ASC, PRODUCT_PRICE_DESC, COUNT_ASC, COUNT_DESC;

    public Sort getSort() {
        return switch (this) {
            case PRODUCT_NAME_ASC -> Sort.by("product.name").ascending();
            case PRODUCT_NAME_DESC -> Sort.by("product.name").descending();
            case PRODUCT_PRICE_ASC -> Sort.by("product.price").ascending();
            case PRODUCT_PRICE_DESC -> Sort.by("product.price").descending();
            case COUNT_ASC -> Sort.by("count").ascending();
            case COUNT_DESC -> Sort.by("count").descending();
            case ID_ASC -> Sort.by("id").ascending();
            case ID_DESC -> Sort.by("id").descending();
        };
    }
}