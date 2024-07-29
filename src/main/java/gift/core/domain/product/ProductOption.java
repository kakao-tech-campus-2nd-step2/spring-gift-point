package gift.core.domain.product;

import gift.core.exception.ErrorCode;
import gift.core.exception.validation.InvalidArgumentException;

public record ProductOption(
        Long id,
        String name,
        Integer quantity
) {
    public static ProductOption of(String name, Integer quantity) {
        return new ProductOption(0L, name, quantity);
    }

    public void validateOrderable(Integer quantity) {
        if (quantity < 0) {
            throw new InvalidArgumentException(ErrorCode.NEGATIVE_QUANTITY);
        }
        if (this.quantity < quantity) {
            throw new InvalidArgumentException(ErrorCode.OPTION_NOT_ENOUGH_QUANTITY);
        }
    }

    public ProductOption applyDecreaseQuantity(Integer quantity) {
        return new ProductOption(id, name, this.quantity - quantity);
    }
}
