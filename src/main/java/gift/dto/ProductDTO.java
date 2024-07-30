package gift.dto;

import gift.domain.Category;
import gift.domain.CategoryName;
import gift.domain.Product;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ProductDTO {

    private Long id;

    @NotBlank(message = "상품 이름은 필수 입력 항목입니다.")
    @Size(max = 15, message = "상품 이름은 최대 15자까지 입력할 수 있습니다.")
    @Pattern(regexp = "^[\\p{L}0-9 ()\\[\\]+\\-&/_]+$", message = "상품 이름에 사용 가능한 특수문자는 ( ), [ ], +, -, &, /, _ 입니다")
    @Pattern(regexp = "^(?!.*(?i)(kakao|카카오).*$).*$", message = "상품 이름에 '카카오'를 사용할 수 없습니다.")
    private String name;

    @NotNull(message = "가격은 필수 입력 항목입니다.")
    @DecimalMin(value = "0.0", inclusive = false, message = "가격은 0보다 커야 합니다.")
    private BigDecimal price;

    private String imageUrl;

    private String description;

    @NotNull(message = "카테고리는 필수 입력 항목입니다.")
    private CategoryName categoryName;

    @NotNull(message = "옵션은 필수 입력 항목입니다.")
    private List<OptionDTO> options = new ArrayList<>();

    public ProductDTO() {}

    private ProductDTO(ProductDTOBuilder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.price = builder.price;
        this.imageUrl = builder.imageUrl;
        this.description = builder.description;
        this.categoryName = builder.categoryName;
        this.options = builder.options != null ? builder.options : new ArrayList<>();
    }

    public static ProductDTO from(Product product) {
        return new ProductDTOBuilder()
            .id(product.getId())
            .name(product.getName())
            .price(product.getPrice())
            .imageUrl(product.getImageUrl())
            .description(product.getDescription())
            .categoryName(product.getCategory().getName())
            .options(product.getOptions().stream()
                .map(OptionDTO::from)
                .collect(Collectors.toList()))
            .build();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public CategoryName getCategoryName() {
        return categoryName;
    }

    public List<OptionDTO> getOptions() {
        return options;
    }

    public static class ProductDTOBuilder {
        private Long id;
        private String name;
        private BigDecimal price;
        private String imageUrl;
        private String description;
        private CategoryName categoryName;
        private List<OptionDTO> options;

        public ProductDTOBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public ProductDTOBuilder name(String name) {
            this.name = name;
            return this;
        }

        public ProductDTOBuilder price(BigDecimal price) {
            this.price = price;
            return this;
        }

        public ProductDTOBuilder imageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
            return this;
        }

        public ProductDTOBuilder description(String description) {
            this.description = description;
            return this;
        }

        public ProductDTOBuilder categoryName(CategoryName categoryName) {
            this.categoryName = categoryName;
            return this;
        }

        public ProductDTOBuilder options(List<OptionDTO> options) {
            this.options = options != null ? options : new ArrayList<>();
            return this;
        }

        public ProductDTO build() {
            return new ProductDTO(this);
        }
    }

    public Product toEntity(Category category) {
        return new Product.ProductBuilder()
            .id(this.id)
            .name(this.name)
            .price(this.price)
            .imageUrl(this.imageUrl)
            .description(this.description)
            .category(category)
            .build();
    }
}
