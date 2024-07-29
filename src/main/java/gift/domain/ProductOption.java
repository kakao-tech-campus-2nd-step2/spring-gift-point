package gift.domain;

import gift.util.page.PageParam;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

public class ProductOption {

    public static class getList extends PageParam {

    }

    public ProductOption() {
    }

    public static class CreateOption {

        @Size(min = 1, max = 50, message = "옵션의 이름은 1부터 50까지 입니다.")
        @Pattern(regexp = "^[가-힣a-zA-Z0-9()\\[\\]+\\-&/_]+$",
            message = "한글, 영어, 숫자, ( ), [ ], +, -, &, /, _만 가능합니다.")
        private String name;
        @Min(value = 1, message = "재고은 최소 1부터 1억 까지 입니다.")
        @Max(value = 100000000, message = "재고은 최소 1부터 1억 까지 입니다.")
        private Long quantity;

        public CreateOption() {
        }

        public CreateOption(String name, Long quantity) {
            this.name = name;
            this.quantity = quantity;
        }

        public String getName() {
            return name;
        }

        public Long getQuantity() {
            return quantity;
        }
    }

    public static class UpdateOption {

        @Size(min = 1, max = 50, message = "옵션의 이름은 1부터 50까지 입니다.")
        @Pattern(regexp = "^[가-힣a-zA-Z0-9()\\[\\]+\\-&/_]+$",
            message = "한글, 영어, 숫자, ( ), [ ], +, -, &, /, _만 가능합니다.")
        private String name;
        @Min(value = 1, message = "재고은 최소 1부터 1억 까지 입니다.")
        @Max(value = 100000000, message = "재고은 최소 1부터 1억 까지 입니다.")
        private Long quantity;

        public UpdateOption() {
        }

        public UpdateOption(String name, Long quantity) {
            this.name = name;
            this.quantity = quantity;
        }

        public String getName() {
            return name;
        }

        public Long getQuantity() {
            return quantity;
        }
    }

    public static class optionSimple {

        private Long productId;
        private Long optionId;
        private String OptionName;
        private Long quantity;

        public optionSimple() {
        }

        public optionSimple(Long productId, Long optionId, String optionName, Long quantity) {
            this.productId = productId;
            this.optionId = optionId;
            OptionName = optionName;
            this.quantity = quantity;
        }

        public Long getProductId() {
            return productId;
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
    }

    public static class optionDetail {

        private Long productId;
        private String productName;
        private Integer productPrice;
        private Long optionId;
        private String OptionName;
        private Long quantity;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public optionDetail() {
        }

        public optionDetail(Long productId, String productName, Integer productPrice,
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
}
