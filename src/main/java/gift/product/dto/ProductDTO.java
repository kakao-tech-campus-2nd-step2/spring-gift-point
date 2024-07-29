package gift.product.dto;

import static gift.product.exception.GlobalExceptionHandler.NOT_EXIST_ID;

import gift.product.exception.InvalidIdException;
import gift.product.model.Product;
import gift.product.repository.CategoryRepository;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public class ProductDTO {
    private Long id;
    @NotBlank(message = "상품명은 필수 입력 요소입니다.")
    @Size(max = 15, message = "입력 가능한 상품명은 공백 포함 최대 15자 입니다.")
    private String name;
    @Positive(message = "상품 가격은 1 이상의 양수만 입력이 가능합니다.")
    private int price;
    @NotBlank(message = "상품 이미지 URL은 필수 입력 요소입니다.")
    private String imageUrl;
    @NotNull(message = "상품의 카테고리가 지정되지 않았습니다.")
    private Long categoryId;

    public ProductDTO() {

    }

    public ProductDTO(String name, int price, String imageUrl, Long categoryId) {
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.categoryId = categoryId;
    }

    public ProductDTO(Long id, String name, int price, String imageUrl, Long categoryId) {
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

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
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

    public Product convertToDomain(CategoryRepository categoryRepository) {
        return new Product(
            name,
            price,
            imageUrl,
            categoryRepository.findById(categoryId)
                .orElseThrow(() -> new InvalidIdException(NOT_EXIST_ID))
        );
    }

    public Product convertToDomain(Long id, CategoryRepository categoryRepository) {
        return new Product(
            id,
            name,
            price,
            imageUrl,
            categoryRepository.findById(categoryId)
                .orElseThrow(() -> new InvalidIdException(NOT_EXIST_ID))
        );
    }
}
