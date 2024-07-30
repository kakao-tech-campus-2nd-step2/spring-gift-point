package gift.domain;

import gift.util.page.PageParam;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

public class Category {

    public Category() {
    }

    public static class getList extends PageParam {

    }

    public static class CreateCategory {

        @NotNull
        private String name;

        public CreateCategory() {
        }

        public CreateCategory(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public static class UpdateCategory {

        @NotNull
        private String name;

        public UpdateCategory() {
        }

        public UpdateCategory(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public static class SimpleCategory {

        private Long CategoryId;
        private String CategoryName;

        public SimpleCategory(Long categoryId, String categoryName) {
            CategoryId = categoryId;
            CategoryName = categoryName;
        }

        public Long getCategoryId() {
            return CategoryId;
        }

        public String getCategoryName() {
            return CategoryName;
        }
    }

    public static class DetailCategory {

        private List<Long> ProductId;
        private Long CategoryId;
        private String CategoryName;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public DetailCategory(List<Long> productId, Long categoryId, String categoryName,
            LocalDateTime createdAt, LocalDateTime updatedAt) {
            ProductId = productId;
            CategoryId = categoryId;
            CategoryName = categoryName;
            this.createdAt = createdAt;
            this.updatedAt = updatedAt;
        }

        public List<Long> getProductId() {
            return ProductId;
        }

        public Long getCategoryId() {
            return CategoryId;
        }

        public String getCategoryName() {
            return CategoryName;
        }

        public LocalDateTime getCreatedAt() {
            return createdAt;
        }

        public LocalDateTime getUpdatedAt() {
            return updatedAt;
        }
    }
}
