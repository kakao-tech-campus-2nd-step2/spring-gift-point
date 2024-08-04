package gift.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "상품 응답 DTO")
public class ProductResponseDto {
    @Schema(description = "상품 고유 id")
    private final Long id;
    @Schema(description = "상품 이름")
    private final String name;
    @Schema(description = "상품 가격")
    private final Integer price;
    @Schema(description = "상품 url")
    private final String imageUrl;
    @Schema(description = "상품 카테고리 id")
    private Long categoryId;
    @Schema(description = "카테고리 이름")
    private String categoryName;

    public ProductResponseDto(Long id, String name, Integer price, String imageUrl) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    public ProductResponseDto(Long id, String name, Integer price, String imageUrl, Long categoryId, String categoryName) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
    }

    public ProductResponseDto(String name, Integer price, String imageUrl) {
        this(null, name, price, imageUrl);
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

    public Long getCategoryId() {
        return categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }
}
