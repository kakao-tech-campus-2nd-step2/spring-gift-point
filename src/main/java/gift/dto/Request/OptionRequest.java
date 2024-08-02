package gift.dto.Request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "옵션 요청")
public class OptionRequest {

    @Schema(description = "옵션 ID", example = "1")
    private Long id;

    @Schema(description = "옵션 수량", example = "2")
    private int quantity;

    public Long getId() {
        return id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
