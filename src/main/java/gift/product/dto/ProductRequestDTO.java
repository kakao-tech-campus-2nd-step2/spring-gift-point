package gift.product.dto;

import gift.category.CategoryDTO;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "상품 요청 DTO")
public class ProductRequestDTO {

    @Schema(description = "상품명")
    private String name;

    @Schema(description = "상품 가격")
    private int price;

    @Schema(description = "상품 이미지 URL")
    private String imageUrl;

    @Schema(description = "상품 카테고리")
    private CategoryDTO category;

    public ProductRequestDTO(
        String name,
        int price,
        String imageUrl,
        CategoryDTO category
    ) {
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.category = category;
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

    public CategoryDTO getCategory() {
        return category;
    }
}
