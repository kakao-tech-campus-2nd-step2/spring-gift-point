package gift.domain;

import gift.util.page.PageParam;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import java.time.LocalDateTime;

public class ProductOrder {

    public static class getList extends PageParam {
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

    public static class OrderDetail {

        private Long productId;
        private String productName;
        private Integer productPrice;
        private Long optionId;
        private String OptionName;
        private Long quantity;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public OrderDetail() {
        }

        public OrderDetail(Long productId, String productName, Integer productPrice,
            Long optionId, String optionName, Long quantity, LocalDateTime createdAt,
            LocalDateTime updatedAt) {
            this.productId = productId;
            this.productName = productName;
            this.productPrice = productPrice;
            this.optionId = optionId;
            OptionName = optionName;
            this.quantity = quantity;
            this.createdAt = createdAt;
            this.updatedAt = updatedAt;
        }

        public Long getProductId() {
            return productId;
        }

        public String getProductName() {
            return productName;
        }

        public Integer getProductPrice() {
            return productPrice;
        }

        public Long getOptionId() {
            return optionId;
        }

        public String getOptionName() {
            return OptionName;
        }

        public Long getQuantity() {
            return quantity;
        }

        public LocalDateTime getCreatedAt() {
            return createdAt;
        }

        public LocalDateTime getUpdatedAt() {
            return updatedAt;
        }
    }

    public static class OrderSimple {
        private Long userId;
        private Long optionId;
        private Long quantity;

        public OrderSimple() {
        }

        public OrderSimple(Long userId, Long optionId, Long quantity) {
            this.userId = userId;
            this.optionId = optionId;
            this.quantity = quantity;
        }

        public Long getUserId() {
            return userId;
        }

        public Long getOptionId() {
            return optionId;
        }

        public Long getQuantity() {
            return quantity;
        }
    }
}
