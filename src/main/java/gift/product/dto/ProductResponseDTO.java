package gift.product.dto;

import gift.category.Category;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Objects;

@Schema(description = "상품 응답 DTO")
public class ProductResponseDTO {

    @Schema(description = "상품 번호")
    private Long id;

    @Schema(description = "상품명")
    private String name;

    @Schema(description = "상품 가격")
    private int price;

    @Schema(description = "상품 이미지 URL")
    private String imageUrl;

    @Schema(description = "상품 카테고리")
    private Category category;

    public ProductResponseDTO(
        Long id,
        String name,
        int price,
        String imageUrl,
        Category category
    ) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.category = category;
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

    public Category getCategory() {
        return category;
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof ProductResponseDTO productResponseDTO) {
            return price == productResponseDTO.price
                   && Objects.equals(id, productResponseDTO.id)
                   && Objects.equals(name, productResponseDTO.name)
                   && Objects.equals(imageUrl, productResponseDTO.imageUrl)
                   && Objects.equals(category, productResponseDTO.category);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, price, imageUrl, category);
    }
}
