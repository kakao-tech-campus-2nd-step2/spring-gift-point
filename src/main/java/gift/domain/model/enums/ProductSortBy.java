package gift.domain.model.enums;

import org.springframework.data.domain.Sort;

public enum ProductSortBy {
    ID_DESC, ID_ASC, PRICE_DESC, PRICE_ASC, NAME_ASC, NAME_DESC;

    public Sort getSort() {
        return switch (this) {
            case ID_ASC -> Sort.by("id").ascending();
            case ID_DESC -> Sort.by("id").descending();
            case PRICE_ASC -> Sort.by("price").ascending();
            case PRICE_DESC -> Sort.by("price").descending();
            case NAME_ASC -> Sort.by("name").ascending();
            case NAME_DESC -> Sort.by("name").descending();
        };
    }
}
