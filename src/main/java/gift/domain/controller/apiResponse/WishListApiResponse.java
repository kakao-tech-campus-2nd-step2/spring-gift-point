package gift.domain.controller.apiResponse;

import com.fasterxml.jackson.annotation.JsonProperty;
import gift.domain.dto.response.WishResponse;
import gift.global.apiResponse.BasicApiResponse;
import java.util.List;
import org.springframework.http.HttpStatusCode;

public class WishListApiResponse extends BasicApiResponse {

    private final List<WishResponse> wishlist;

    public WishListApiResponse(
        @JsonProperty(value = "status", required = true) HttpStatusCode statusCode,
        @JsonProperty(value = "wishlist", required = true) List<WishResponse> wishlist
    ) {
        super(statusCode);
        this.wishlist = wishlist;
    }

    public List<WishResponse> getWishlist() {
        return wishlist;
    }
}
