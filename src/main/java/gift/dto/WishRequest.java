package gift.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Request DTO for creating or updating a Wish")
public class WishRequest {

    @Schema(description = "ID of the product", example = "1")
    private Long productId;
//    @Schema(description = "Quantity of the product", example = "5")
//    private int number;


    public WishRequest() {
    }

    public WishRequest(Long product_id) {
        this.productId = product_id;
    }

//    public int getNumber() {
//        return number;
//    }

    public Long getProductId() {
        return productId;
    }
}
