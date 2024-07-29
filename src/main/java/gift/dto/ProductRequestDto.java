package gift.dto;

import gift.domain.Category;
import gift.domain.Option;
import gift.domain.Product;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class ProductRequestDto {
    private Long id;
    @NotBlank(message = "상품 이름을 비우거나 공백으로 설정할 수 없습니다")
    @Size(max=15,message = "상품명은 공백 포함하여 최대 15자까지 입력할 수 있습니다")
    @Pattern(regexp = "^[\\w\\s\\(\\)\\[\\]\\+\\-\\&\\/\\_가-힣]*$", message = "특수 문자는 ( ), [ ], +, -, &, /, _ 만 사용할 수 있습니다.")
    @Pattern(regexp = "^(?!.*카카오).*$", message = "'카카오'가 포함된 상품 이름은 담당 MD와 협의한 후에 사용할 수 있습니다.")
    @Column(name = "name", nullable = false)
    private String name;
    private int price;
    private String imageUrl;
    private Long categoryId;

    public ProductRequestDto(Long id, String name, int price, String imageUrl, Long categoryId) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.categoryId = categoryId;
    }

    public Long getId() {
        return id;
    }
    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public Product toEntity(Category category) {
        return new Product(getName(),getPrice(),getImageUrl(),category);
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }
}
