package gift.product.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Objects;

@Schema(description = "상품 페이지네이션 응답용 DTO")
public class ProductPaginationResponseDTO {

    @Schema(description = "상품 id")
    private long id;

    @Schema(description = "상품명")
    private String name;

    @Schema(description = "가격")
    private int price;

    @Schema(description = "상품 이미지 url")
    private String imageURL;

    public ProductPaginationResponseDTO(long id, String name, int price, String imageURL) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageURL = imageURL;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getImageURL() {
        return imageURL;
    }

    public int getPrice() {
        return price;
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof ProductPaginationResponseDTO productPaginationResponseDTO) {
            return id == productPaginationResponseDTO.id
                   && price == productPaginationResponseDTO.price
                   && Objects.equals(name, productPaginationResponseDTO.name)
                   && Objects.equals(imageURL, productPaginationResponseDTO.imageURL);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, price, imageURL);
    }
}
