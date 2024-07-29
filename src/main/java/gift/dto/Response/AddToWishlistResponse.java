package gift.dto.Response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "위시리스트 추가 응답")
public class AddToWishlistResponse {

    @Schema(description = "성공 여부", example = "true")
    private boolean success;

    public AddToWishlistResponse(boolean success) {
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
