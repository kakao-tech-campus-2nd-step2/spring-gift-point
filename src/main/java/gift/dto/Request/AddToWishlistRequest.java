package gift.dto.Request;

import java.util.List;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "위시리스트에 추가할 상품 정보 요청")
public class AddToWishlistRequest {

    @Schema(description = "상품 ID", example = "1")
    private Long productId;

    @Schema(description = "상품 수량", example = "2")
    private int quantity;

    @Schema(description = "옵션 목록")
    private List<OptionRequest> options;

    public Long getProductId() {
        return productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public List<OptionRequest> getOptions() {
        return options;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setOptions(List<OptionRequest> options) {
        this.options = options;
    }
}
