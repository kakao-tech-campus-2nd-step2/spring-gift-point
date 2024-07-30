package gift.domain;

import gift.util.page.PageParam;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

public class Product {

    private Product() {
    }

    public static class getList extends PageParam {

    }

    public static class CreateProduct {

        @NotNull(message = "name은 필수 입니다.")
        @Size(max = 15, message = "value는 15자 이상 초과 할 수 없습니다.")
        @Pattern(regexp = "^[가-힣a-zA-Z0-9\\s\\(\\)\\[\\]\\+\\-\\&\\/_]*$",
            message = "특수 문자는 ( ), [ ], +, -, &, /, _ 만 사용할 수 있습니다.")
        @Pattern(regexp = "^(?!.*카카오).*$",
            message = "카카오가 포함된 문구는 담당 MD와 협의한 경우에만 사용할 수 있습니다.")
        private String name;
        @NotNull(message = "price은 필수 입니다.")
        private Integer price;
        @NotNull(message = "imageUrl은 필수 입니다.")
        private String imageUrl;
        @NotNull
        private Long categoryId;
        @Size(min = 1, max = 50, message = "옵션의 이름은 1부터 50까지 입니다.")
        @Pattern(regexp = "^[가-힣a-zA-Z0-9()\\[\\]+\\-&/_]+$",
            message = "한글, 영어, 숫자, ( ), [ ], +, -, &, /, _만 가능합니다.")
        private String OptionName;
        @Min(value = 1, message = "재고은 최소 1부터 1억 까지 입니다.")
        @Max(value = 100000000, message = "재고은 최소 1부터 1억 까지 입니다.")
        private Long quantity;

        public CreateProduct(String name, Integer price, String imageUrl, Long categoryId,
            String OptionName, Long quantity) {
            this.name = name;
            this.price = price;
            this.imageUrl = imageUrl;
            this.categoryId = categoryId;
            this.OptionName = OptionName;
            this.quantity = quantity;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getPrice() {
            return price;
        }

        public void setPrice(Integer price) {
            this.price = price;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        public Long getCategoryId() {
            return categoryId;
        }

        public void setCategoryId(Long categoryId) {
            this.categoryId = categoryId;
        }

        public String getOptionName() {
            return OptionName;
        }

        public Long getQuantity() {
            return quantity;
        }
    }

    public static class UpdateProduct {

        @NotNull(message = "name은 필수 입니다.")
        @Size(max = 15, message = "value는 15자 이상 초과 할 수 없습니다.")
        @Pattern(regexp = "^[가-힣a-zA-Z0-9\\s\\(\\)\\[\\]\\+\\-\\&\\/_]*$",
            message = "특수 문자는 ( ), [ ], +, -, &, /, _ 만 사용할 수 있습니다.")
        @Pattern(regexp = "^(?!.*카카오).*$",
            message = "카카오가 포함된 문구는 담당 MD와 협의한 경우에만 사용할 수 있습니다.")
        private String name;
        @NotNull(message = "price은 필수 입니다.")
        private Integer price;
        @NotNull(message = "imageUrl은 필수 입니다.")
        private String imageUrl;
        @NotNull
        private Long categoryId;

        public UpdateProduct(String name, Integer price, String imageUrl, Long categoryId) {
            this.name = name;
            this.price = price;
            this.imageUrl = imageUrl;
            this.categoryId = categoryId;
        }

        public String getName() {
            return name;
        }

        public Integer getPrice() {
            return price;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public Long getCategoryId() {
            return categoryId;
        }

        public void setCategoryId(Long categoryId) {
            this.categoryId = categoryId;
        }
    }

    public static class ProductSimple {

        private Long id;
        private String name;

        public ProductSimple(Long id, String name) {
            this.id = id;
            this.name = name;
        }

        public Long getId() {
            return id;
        }

        public String getName() {
            return name;
        }
    }

    public static class ProductDetail {

        private Long id;
        private String name;
        private Integer price;
        private String imageUrl;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private List<Long> wishUserId;
        private Long categoryId;

        public ProductDetail(Long id, String name, Integer price, String imageUrl,
            LocalDateTime createdAt, LocalDateTime updatedAt, List<Long> wishUserId,
            Long categoryId) {
            this.id = id;
            this.name = name;
            this.price = price;
            this.imageUrl = imageUrl;
            this.createdAt = createdAt;
            this.updatedAt = updatedAt;
            this.wishUserId = wishUserId;
            this.categoryId = categoryId;
        }

        public Long getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public Integer getPrice() {
            return price;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public LocalDateTime getCreatedAt() {
            return createdAt;
        }

        public LocalDateTime getUpdatedAt() {
            return updatedAt;
        }

        public List<Long> getWishUserId() {
            return wishUserId;
        }

        public Long getCategoryId() {
            return categoryId;
        }
    }

}
