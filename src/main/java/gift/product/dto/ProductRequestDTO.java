package gift.product.dto;

import gift.category.CategoryDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Objects;

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

    @Override
    public boolean equals(Object object) {
        if (object instanceof ProductRequestDTO productRequestDTO) {
            return price == productRequestDTO.price
                   && Objects.equals(name, productRequestDTO.name)
                   && Objects.equals(imageUrl, productRequestDTO.imageUrl)
                   && Objects.equals(category, productRequestDTO.category);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, price, imageUrl, category);
    }
}
