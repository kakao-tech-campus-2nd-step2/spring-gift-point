package gift.dto.Response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "위시리스트 응답")
public class WishlistResponse {

    @Schema(description = "성공 여부", example = "true")
    private boolean success;

    @Schema(description = "총 가격", example = "15000")
    private double totalPrice;

    public WishlistResponse(boolean success) {
        this.success = success;
    }

    public WishlistResponse(boolean success, double totalPrice) {
        this.success = success;
        this.totalPrice = totalPrice;
    }

    public boolean isSuccess() {
        return success;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }
}
