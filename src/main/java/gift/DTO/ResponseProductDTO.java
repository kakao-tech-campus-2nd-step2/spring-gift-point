package gift.DTO;

import gift.Model.Entity.Product;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "단일 상품 조회 응답 DTO")
public class ResponseProductDTO {
    @Schema(description = "상품 Id")
    private Long id;

    @Schema(description = "상품 이름")
    private String name;

    @Schema(description = "상품 가격")
    private int price;

    @Schema(description = "상품 이미지url")
    private String imageUrl;

    @Schema(description = "상품 옵션")
    private List<ResponseOptionDTO> options;

    public ResponseProductDTO(Long id, String name, int price, String imageUrl, List<ResponseOptionDTO> options) {
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

    public List<ResponseOptionDTO> getOptions() {
        return options;
    }

    public static ResponseProductDTO of(Product product, List<ResponseOptionDTO> options) {
        return new ResponseProductDTO(product.getId(), product.getName().getValue(), product.getPrice().getValue(), product.getImageUrl().getValue(), options);
    }
}
