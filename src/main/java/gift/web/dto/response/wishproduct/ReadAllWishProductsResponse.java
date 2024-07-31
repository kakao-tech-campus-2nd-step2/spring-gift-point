package gift.web.dto.response.wishproduct;

import java.util.List;

public class ReadAllWishProductsResponse {

    private List<ReadWishProductResponse> wishlist;

    private ReadAllWishProductsResponse() {
    }

    public ReadAllWishProductsResponse(List<ReadWishProductResponse> wishlist) {
        this.wishlist = wishlist;
    }

    public List<ReadWishProductResponse> getWishlist() {
        return wishlist;
    }
}
