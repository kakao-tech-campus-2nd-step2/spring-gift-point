package gift.domain;

import gift.util.page.PageParam;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
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

        @NotNull
        private String imageURL;

        @NotNull
        private String title;

        private String description;

        @Pattern(regexp = "#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})", message = "hex코드만 입력이 가능합니다.")
        @NotNull
        private String backgroundColor;

        public CreateCategory() {
        }

        public CreateCategory(String name, String imageURL, String title, String description,
            String backgroundColor) {
            this.name = name;
            this.imageURL = imageURL;
            this.title = title;
            this.description = description;
            this.backgroundColor = backgroundColor;
        }

        public String getName() {
            return name;
        }

        public String getImageURL() {
            return imageURL;
        }

        public String getTitle() {
            return title;
        }

        public String getDescription() {
            return description;
        }

        public String getBackgroundColor() {
            return backgroundColor;
        }
    }

    public static class UpdateCategory {

        @NotNull
        private String name;

        @NotNull
        private String imageURL;

        @NotNull
        private String title;

        private String description;

        @Pattern(regexp = "#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})", message = "hex코드만 입력이 가능합니다.")
        @NotNull
        private String backgroundColor;

        public UpdateCategory() {
        }

        public UpdateCategory(String name, String imageURL, String title, String description,
            String backgroundColor) {
            this.name = name;
            this.imageURL = imageURL;
            this.title = title;
            this.description = description;
            this.backgroundColor = backgroundColor;
        }

        public String getName() {
            return name;
        }

        public String getImageURL() {
            return imageURL;
        }

        public String getTitle() {
            return title;
        }

        public String getDescription() {
            return description;
        }

        public String getBackgroundColor() {
            return backgroundColor;
        }
    }

    public static class SimpleCategory {

        private Long CategoryId;
        private String label;
        private String imageURL;
        private String title;
        private String description;
        private String backgroundColor;

        public SimpleCategory(Long categoryId, String label, String imageURL, String title,
            String description, String backgroundColor) {
            CategoryId = categoryId;
            this.label = label;
            this.imageURL = imageURL;
            this.title = title;
            this.description = description;
            this.backgroundColor = backgroundColor;
        }

        public Long getCategoryId() {
            return CategoryId;
        }


        public String getLabel() {
            return label;
        }

        public String getImageURL() {
            return imageURL;
        }

        public String getTitle() {
            return title;
        }

        public String getDescription() {
            return description;
        }

        public String getBackgroundColor() {
            return backgroundColor;
        }
    }

    public static class DetailCategory {

        private List<Long> ProductId;
        private Long CategoryId;
        private String label;
        private String imageURL;
        private String title;
        private String description;
        private String backgroundColor;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;


        public DetailCategory(List<Long> productId, Long categoryId, String label,
            String imageURL, String title, String description, String backgroundColor,
            LocalDateTime createdAt, LocalDateTime updatedAt) {
            ProductId = productId;
            CategoryId = categoryId;
            this.label = label;
            this.imageURL = imageURL;
            this.title = title;
            this.description = description;
            this.backgroundColor = backgroundColor;
            this.createdAt = createdAt;
            this.updatedAt = updatedAt;
        }

        public List<Long> getProductId() {
            return ProductId;
        }

        public Long getCategoryId() {
            return CategoryId;
        }

        public String getLabel() {
            return label;
        }

        public String getImageURL() {
            return imageURL;
        }

        public String getTitle() {
            return title;
        }

        public String getDescription() {
            return description;
        }

        public String getBackgroundColor() {
            return backgroundColor;
        }

        public LocalDateTime getCreatedAt() {
            return createdAt;
        }

        public LocalDateTime getUpdatedAt() {
            return updatedAt;
        }
    }
}
