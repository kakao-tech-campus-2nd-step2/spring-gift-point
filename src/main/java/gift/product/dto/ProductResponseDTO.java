package gift.product.dto;

import gift.option.dto.OptionResponseDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
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

    @Schema(description = "상품 옵션 리스트")
    private List<OptionResponseDTO> options;

    public ProductResponseDTO(
        Long id,
        String name,
        int price,
        String imageUrl,
        List<OptionResponseDTO> options
    ) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.options = options;
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

    public List<OptionResponseDTO> getOptions() {
        return options;
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof ProductResponseDTO productResponseDTO) {
            return price == productResponseDTO.price
                   && Objects.equals(id, productResponseDTO.id)
                   && Objects.equals(name, productResponseDTO.name)
                   && Objects.equals(imageUrl, productResponseDTO.imageUrl)
                   && Objects.equals(options, productResponseDTO.options);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, price, imageUrl, options);
    }
}
