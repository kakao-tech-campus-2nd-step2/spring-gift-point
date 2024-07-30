package gift.domain;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public class ProductOrder {

    public ProductOrder() {
    }

    public static class decreaseProductOption {

        @Min(value = 1, message = "재고은 최소 1부터 1억 까지 입니다.")
        @Max(value = 100000000, message = "재고은 최소 1부터 1억 까지 입니다.")
        private Long quantity;

        public decreaseProductOption() {
        }

        public decreaseProductOption(Long quantity) {
            this.quantity = quantity;
        }

        public Long getQuantity() {
            return quantity;
        }
    }
}
