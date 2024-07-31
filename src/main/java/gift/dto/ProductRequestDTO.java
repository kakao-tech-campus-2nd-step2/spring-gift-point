package gift.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ProductRequestDTO {
    private Long id;

    @NotBlank(message = "상품 이름은 필수 입력 사항입니다.")
    private String name;

    @NotNull(message = "가격을 입력해주세요.")
    private int price;

    private String imageUrl;

    @NotNull(message = "카테고리를 선택해주세요.")
    private Long categoryId;

    public ProductRequestDTO() {
    }

    public ProductRequestDTO(Long id, String name, int price, String imageUrl, Long categoryId) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.categoryId = categoryId;
    }

    public ProductRequestDTO(String name, int price, String imageUrl, Long categoryId) {
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.categoryId = categoryId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public @NotNull(message = "카테고리를 선택해주세요.") Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(@NotNull(message = "카테고리를 선택해주세요.") Long categoryId) {
        this.categoryId = categoryId;
    }
}
